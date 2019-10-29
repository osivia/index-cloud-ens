/**
 * 
 */
package fr.index.cloud.ens.virusscan;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;


/**
 * Scan new files thanks to ICAP compatible scanner
 * 
 * @author jean-sébastien steux
 *
 */
public class ScanListener implements EventListener {

    /** Log. */
    private static final Log log = LogFactory.getLog(ScanListener.class);

    private static String DEFAULT_ERROR_VIRUS_FOUND_MESSAGE = "Virus found";
    private static String ERROR_VIRUS_FOUND_LOCALIZED_MESSAGE = "label.error.index.custom.virusFound";


    int maxPoolSize = 50;
    int poolSize = 20;
    long keepAliveTime = 30;
    long timeout = 30;

    protected ExecutorService threadPool = null;

    protected ExecutorService getThreadPool() {

        if (threadPool == null) {
            ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(maxPoolSize);
            threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
        }

        return threadPool;
    }


    @Override
    public void handleEvent(Event event) throws ClientException {

        if (log.isDebugEnabled())
            log.debug("ScanListener.handleEvent " + event.getName());

        if ((event.getContext() instanceof DocumentEventContext) && (DocumentEventTypes.ABOUT_TO_CREATE.equals(event.getName()))
                || (DocumentEventTypes.ABOUT_TO_IMPORT.equals(event.getName()))) {
            DocumentEventContext evtCtx = (DocumentEventContext) event.getContext();

            DocumentModel docToCreate = evtCtx.getSourceDocument();


            // BlobHolder is defined for document having file
            BlobHolder bHolder = docToCreate.getAdapter(BlobHolder.class);
            if (bHolder != null && bHolder.getBlob() != null) {

                try {

                    if (log.isDebugEnabled())
                        log.debug("ScanListener.scan " + bHolder.getBlob().getFilename());


                    ICAP icap = new ICAP("icap", 1344, "avscan", bHolder.getBlob().getStream(), bHolder.getBlob().getLength());

                    Future<ICAPResult> future = getThreadPool().submit(icap);

                    ICAPResult result = null;

                    result = future.get(timeout, TimeUnit.SECONDS);

                    removeFromQuarantine(docToCreate);

                    if (result != null && result.getStateProcessing() == ICAPResult.STATE_VIRUS_FOUND) {
                        event.markBubbleException();

                        throw new VirusScanException(DEFAULT_ERROR_VIRUS_FOUND_MESSAGE, ERROR_VIRUS_FOUND_LOCALIZED_MESSAGE, null);
                    }


                } catch (Exception ex) {
                    
                    if( ex instanceof VirusScanException)    {
                        // The document is not created
                        throw (VirusScanException) ex;
                    }
                    
                    // Other errors are logged
                    if (ex instanceof TimeoutException)
                        log.warn("Timeout during scan of " + bHolder.getBlob().getFilename() + ". File is put in quarantine .");
                    else
                        log.error("error during scan of " + bHolder.getBlob().getFilename() + ". File is put in quarantine .", ex);

                    addToQuarantine(docToCreate);
                }
            }
        }

    }


    private void addToQuarantine(DocumentModel docToCreate) {
        if (docToCreate.getProperty("virusScan", "quarantineDate") == null)
            docToCreate.setProperty("virusScan", "quarantineDate", new java.util.Date());
    }


    private void removeFromQuarantine(DocumentModel docToCreate) {
        docToCreate.setProperty("virusScan", "quarantineDate", null);
    }

}
