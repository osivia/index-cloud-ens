package fr.index.cloud.ens.ext.etb;

import org.osivia.portal.api.cache.services.IGlobalParameters;

/**
 * Données renvoyées par le service etablissement
 * 
 * @author Jean-Sébastien
 */
public class EtablissementBean implements IGlobalParameters {
  
     
    
    /** nom de l'établissement **/   
    private String nom;    
    /** code de l'établissement */
    private String code;     

 
    public EtablissementBean()  {
        super();
    }
    
    public EtablissementBean(String code, String nom) {
        super();
        this.code = code;
        this.nom = nom;
    }



    
    /**
     * Getter for code.
     * @return the code
     */
    public String getCode() {
        return code;
    }

    
    /**
     * Setter for code.
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter for nom.
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    
    /**
     * Setter for nom.
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EtablissementBean other = (EtablissementBean) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        return true;
    }
    

}
