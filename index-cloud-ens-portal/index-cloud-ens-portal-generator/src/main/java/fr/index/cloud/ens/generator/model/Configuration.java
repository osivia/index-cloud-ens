package fr.index.cloud.ens.generator.model;


/**
 * Generator configuration java-bean.
 *
 * @author Jean-Sébastien Steux
 */
public class Configuration {

    
    private Integer nbOfUsers;
 
    private Integer nbOfRootFolers;

    private Integer nbOfSubFolers;

    private Integer nbOfSubItems;


	/**
	 * @return the nbOfUsers
	 */
	public Integer getNbOfUsers() {
		return nbOfUsers;
	}

	/**
	 * @param nbOfUsers the nbOfUsers to set
	 */
	public void setNbOfUsers(Integer nbOfUsersPerWks) {
		this.nbOfUsers = nbOfUsersPerWks;
	}

	/**
	 * @return the nbOfRootFolers
	 */
	public Integer getNbOfRootFolers() {
		return nbOfRootFolers;
	}

	/**
	 * @param nbOfRootFolers the nbOfRootFolers to set
	 */
	public void setNbOfRootFolers(Integer nbOfRootFolers) {
		this.nbOfRootFolers = nbOfRootFolers;
	}

	/**
	 * @return the nbOfSubFolers
	 */
	public Integer getNbOfSubFolers() {
		return nbOfSubFolers;
	}

	/**
	 * @param nbOfSubFolers the nbOfSubFolers to set
	 */
	public void setNbOfSubFolers(Integer nbOfSubFolers) {
		this.nbOfSubFolers = nbOfSubFolers;
	}

	/**
	 * @return the nbOfSubItems
	 */
	public Integer getNbOfSubItems() {
		return nbOfSubItems;
	}

	/**
	 * @param nbOfSubItems the nbOfSubItems to set
	 */
	public void setNbOfSubItems(Integer nbOfSubItems) {
		this.nbOfSubItems = nbOfSubItems;
	}


}
