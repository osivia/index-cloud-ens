/**
 * 
 */
package fr.index.cloud.ens.virusscan;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DataModel;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;


/**
 * Scan new files thanks to ICAP compatible scanner
 * 
 * @author jean-sébastien steux
 *
 */
public class ScanListener implements EventListener {

    /** Log. */
    private static final Log log = LogFactory.getLog(ScanListener.class);

    /** Dont' modify. Used at portal level (NuxeoException) */
    private static String DEFAULT_ERROR_VIRUS_FOUND_MESSAGE = "Virus found";

    private static String ERROR_VIRUS_FOUND_LOCALIZED_MESSAGE = "label.error.index.custom.virusFound";


    /** The max pool size. */
    int maxPoolSize = 50;

    /** The pool size. */
    int poolSize = 20;

    /** The keep alive time. */
    long keepAliveTime = 30;

    /** The timeout. */
    long timeout = 10;

    protected ExecutorService threadPool = null;

    /**
     * build the pool
     * 
     * @return
     */
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

        if ((event.getContext() instanceof DocumentEventContext) && (DocumentEventTypes.BEFORE_DOC_UPDATE.equals(event.getName()))) {

            DocumentEventContext evtCtx = (DocumentEventContext) event.getContext();

            DocumentModel docToUpdate = evtCtx.getSourceDocument();


            if (docToUpdate != null) {
                DataModel dm = docToUpdate.getDataModel("file");
                if (dm != null && dm.isDirty())
                    checkFile(event, docToUpdate);
            }

        }


        if ((event.getContext() instanceof DocumentEventContext) && (DocumentEventTypes.ABOUT_TO_CREATE.equals(event.getName()))
                || (DocumentEventTypes.ABOUT_TO_IMPORT.equals(event.getName()))) {
            DocumentEventContext evtCtx = (DocumentEventContext) event.getContext();

            DocumentModel docToCreate = evtCtx.getSourceDocument();


            checkFile(event, docToCreate);
        }

    }


    /**
     * check the file and throws a VirusScanException if a virus is detected
     * 
     * 
     * @param event
     * @param docToCreate
     */
    private void checkFile(Event event, DocumentModel docToCreate) {

        boolean virusFound = false;

        // BlobHolder is defined for document having file
        BlobHolder bHolder = docToCreate.getAdapter(BlobHolder.class);
        if (bHolder != null && bHolder.getBlob() != null) {

            try {

                if (log.isDebugEnabled())
                    log.debug("ScanListener.scan " + bHolder.getBlob().getFilename());

                String ICAPHost = Framework.getProperty("index.antivirus.icap.host");
                String ICAPPort = Framework.getProperty("index.antivirus.icap.port");

                if (StringUtils.isNotEmpty(ICAPHost) && StringUtils.isNotEmpty(ICAPPort)) {

                    ICAP icap = new ICAP(ICAPHost, Integer.parseInt(ICAPPort), "avscan", bHolder.getBlob().getStream(), bHolder.getBlob().getLength());

                    Future<ICAPResult> future = getThreadPool().submit(icap);

                    ICAPResult result = future.get(timeout, TimeUnit.SECONDS);

                    // No technical errors : either safe or contains a virus
                    // Anyway remove from quarantine
                    removeFromQuarantine(docToCreate);
                    
                    if (result != null && result.getStateProcessing() == ICAPResult.STATE_VIRUS_FOUND) {
                        log.warn("Virus found in " + bHolder.getBlob().getFilename() + ".");
                        virusFound = true;
                    }                    
                }

            } catch (Exception e) {

                // If the file can not have be scanned, it must be pu in quarantine
                addToQuarantine(docToCreate);

                boolean mustLogError = true;

                if (e instanceof TimeoutException) {
                    // Timeout -> no stack
                    log.warn("Timeout during scan of " + bHolder.getBlob().getFilename() + " . File is put in quarantine .");
                    mustLogError = false;
                }

                if (e instanceof ExecutionException) {
                    ExecutionException exec = (ExecutionException) e;
                    if (exec.getCause() instanceof UnknownHostException || exec.getCause() instanceof IOException) {
                        // IO error -> no stack
                        log.error("error during scan of " + bHolder.getBlob().getFilename() + " : " + exec.getCause().toString()
                                + ".  File is put in quarantine .");

                        mustLogError = false;
                    }
                }

                if (mustLogError)
                    // all other errors : stack for easier diagnostic
                    log.error("error during scan of " + bHolder.getBlob().getFilename() + " . File is put in quarantine .", e);
            }

            
           if( virusFound)  {
               event.markBubbleException();
               throw new VirusScanException(DEFAULT_ERROR_VIRUS_FOUND_MESSAGE, ERROR_VIRUS_FOUND_LOCALIZED_MESSAGE, null);
           }
        }
    }


    /**
     * Add to quarantine
     * 
     * @param docToCreate
     */
    private void addToQuarantine(DocumentModel docToCreate) {
        if (docToCreate.getProperty("virusScan", "quarantineDate") == null)
            docToCreate.setProperty("virusScan", "quarantineDate", new java.util.Date());
    }


    /**
     * Remove from quaratine (if needed)
     * 
     * @param docToCreate
     */
    private void removeFromQuarantine(DocumentModel docToCreate) {
        docToCreate.setProperty("virusScan", "quarantineDate", null);
    }

}
