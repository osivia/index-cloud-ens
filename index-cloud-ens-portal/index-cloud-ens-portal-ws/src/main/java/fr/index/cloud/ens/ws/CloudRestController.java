package fr.index.cloud.ens.ws;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CloudRestController {
	/**
	 * URL to use: https://cloud-ens.osivia.org/index-cloud-portal-ens-ws/rest/documents
	 * 
	 * @return list of users as JSON
	 */
	@RequestMapping(value = "/documents", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)

	public List<DocumentBean> getDocuments(HttpServletRequest request) {

		List<DocumentBean> docBeans = new ArrayList();
		docBeans.add(new DocumentBean("test"));
		return docBeans;
	}
	
}
