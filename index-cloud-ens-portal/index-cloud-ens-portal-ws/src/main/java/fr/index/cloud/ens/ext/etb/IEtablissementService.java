package fr.index.cloud.ens.ext.etb;


/**
 * Service de récupération des informations établissement
 * 
 * @author Jean-Sébastien
 */
public interface IEtablissementService {
    
    /**
     * Renvoie en fonction du code de l'établissement les informations associées
     * à ce dernier
     * 
     * Ces informations sont basées sur un Web-Service PRONOTE
     * 
     * @param code identifiant de l'application pronote
     * @return
     */
    public EtablissementBean getEtablissement(String code) ;

}
