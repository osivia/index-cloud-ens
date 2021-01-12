#copy secret key from a store to a new store

java -cp .\target\index-cloud-ens-utils-security-1.0-SNAPSHOT.jar fr.index.cloud.ens.utils.security.extraction.ExtractSecretKey c:\datas\security\aes-keystore.jck nuxeo-binaries <storepass> <keypass> c:\datas\security\output.jck




