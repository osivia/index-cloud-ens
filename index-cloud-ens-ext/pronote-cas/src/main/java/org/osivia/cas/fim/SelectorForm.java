/**
 * 
 */
package org.osivia.cas.fim;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Form of the selector view
 * @author Loïc Billon
 *
 */
@Component
public class SelectorForm {
	
	private String baseAcaUrl;
	
	private String baseSupUrl;

	private String acaUrl;
	
	private String supUrl;
	
	public String getBaseAcaUrl() {
		return baseAcaUrl;
	}

	@Value(value="${fim.aca.baseurl}")
	public void setBaseAcaUrl(String baseAcaUrl) {
		this.baseAcaUrl = baseAcaUrl;
	}

	public String getBaseSupUrl() {
		return baseSupUrl;
	}
	
	@Value(value="${fim.sup.baseurl}")
	public void setBaseSupUrl(String baseSupUrl) {
		this.baseSupUrl = baseSupUrl;
	}

	public String getAcaUrl() {
		return acaUrl;
	}

	public void setAcaUrl(String acaUrl) {
		this.acaUrl = acaUrl;
	}

	public String getSupUrl() {
		return supUrl;
	}

	public void setSupUrl(String supUrl) {
		this.supUrl = supUrl;
	}
	
	
}
