
package fr.index.cloud.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import fr.index.cloud.oauth.authentication.PortalUserDetailService;
import fr.index.cloud.oauth.tokenStore.PortalTokenStore;
import fr.index.security.oauth.approval.SparklrUserApprovalHandler;
import fr.index.security.oauth.services.PhotoService;
import fr.index.security.oauth.services.mvc.AccessConfirmationController;
import fr.index.security.oauth.services.mvc.AdminController;
import fr.index.security.oauth.services.mvc.PhotoController;


@Configuration
public class OAuth2ServerConfig {

	private static final String CLOUD_RESOURCE_ID = "cloud";

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.resourceId(CLOUD_RESOURCE_ID).stateless(false);
		}

		
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				// Since we want the protected resources to be accessible in the UI as well we need 
				// session creation to be allowed (it's disabled by default in 2.0.6)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.and()
				.requestMatchers().antMatchers("/rest/Drive.**", "/rest/Admin.**","/photos/**", "/oauth/users/**", "/oauth/clients/**","/me")
			.and()
				.authorizeRequests()
				    .antMatchers("/rest/Drive.**").access("#oauth2.hasScope('drive')")
				    .antMatchers("/rest/Admin.**").access("hasRole('ROLE_ADMIN')")
					.antMatchers("/me").access("#oauth2.hasScope('read')")					
					.antMatchers("/photos").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")                                        
					.antMatchers("/photos/trusted/**").access("#oauth2.hasScope('trust')")
					.antMatchers("/photos/user/**").access("#oauth2.hasScope('trust')")	
					
					.antMatchers("/photos/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
					.regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
						.access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
					.regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
						.access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
					.regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
						.access("#oauth2.clientHasRole('ROLE_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')");
			
			
			
			
			// @formatter:on
		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Autowired
		private UserApprovalHandler userApprovalHandler;

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;
		
	      @Autowired
	        private PhotoService photoService;
	      
          @Autowired
          private PhotoController photoController;	      

		@Value("${tonr.redirect:http://localhost:8080/tonr2/sparklr/redirect}")
		private String tonrRedirectUri;
		
	      @Autowired
	        private AccessConfirmationController accessConfirmationController;
	      
	        @Autowired
	        private ClientDetailsService clientDetailsService;	   
	        @Autowired
	        public ApprovalStore approvalStore;
	        @Autowired
	        AdminController adminController;
	        
	        @Autowired
	        ConsumerTokenServices tokenServices;
	        
	        @Autowired
	        PortalUserDetailService userDetailService; 


		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		    System.out.println("osivia configuration");
		    
            accessConfirmationController.setClientDetailsService(clientDetailsService);
            accessConfirmationController.setApprovalStore(approvalStore);


            // @Bean
            // public AdminController adminController(TokenStore tokenStore,
            // @Qualifier("consumerTokenServices") ConsumerTokenServices tokenServices,
            // SparklrUserApprovalHandler userApprovalHandler) {
            // AdminController adminController = new AdminController();
            adminController.setTokenStore(tokenStore);
            adminController.setTokenServices(tokenServices);
            adminController.setUserApprovalHandler((SparklrUserApprovalHandler) userApprovalHandler);
            // return adminController;
            // }

            photoController.setPhotoService(photoService);

		        
			// @formatter:off
			clients.inMemory().withClient("tonr")
			 			.resourceIds(CLOUD_RESOURCE_ID)
			 			.authorizedGrantTypes("authorization_code", "implicit")
			 			.authorities("ROLE_CLIENT")
			 			.scopes("read", "write")
			 			.secret("secret")
			 		.and()
			 		.withClient("tonr-with-redirect")
			 			.resourceIds(CLOUD_RESOURCE_ID)
			 			.authorizedGrantTypes("authorization_code", "implicit")
			 			.authorities("ROLE_CLIENT")
			 			.scopes("read", "write")
			 			.secret("secret")
			 			.redirectUris(tonrRedirectUri)
			 		.and()
		 		    .withClient("my-client-with-registered-redirect")
	 			        .resourceIds(CLOUD_RESOURCE_ID)
	 			        .authorizedGrantTypes("authorization_code", "client_credentials")
	 			        .authorities("ROLE_CLIENT")
	 			        .scopes("read", "write", "trust")
                        .secret("secret")	 			        
//	 			        .redirectUris("http://anywhere?key=value")
		 		    .and()
	 		        .withClient("my-trusted-client")
 			            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
 			            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
 			            .scopes("read", "write", "trust")
 			            .accessTokenValiditySeconds(60)
		 		    .and()
	 		        .withClient("pronote1234")
 			            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
 			            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
 			            .scopes("drive")
 			            .secret("secret1234")
 			           .accessTokenValiditySeconds(60)
	 		        .and()
 		            .withClient("my-less-trusted-client")
			            .authorizedGrantTypes("authorization_code", "implicit")
			            .authorities("ROLE_CLIENT")
			            .scopes("read", "write", "trust")
     		        .and()
		            .withClient("my-less-trusted-autoapprove-client")
		                .authorizedGrantTypes("implicit")
		                .authorities("ROLE_CLIENT")
		                .scopes("read", "write", "trust")
		                .autoApprove(true);
			// @formatter:on
		}

		@Bean
		public TokenStore tokenStore() {
			//return new InMemoryTokenStore();
			
			return new PortalTokenStore();
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
					.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer.realm("cloud/client");
		}

	}

	protected static class Stuff {

		@Autowired
		private ClientDetailsService clientDetailsService;

		@Autowired
		private TokenStore tokenStore;

		@Bean
		public ApprovalStore approvalStore() throws Exception {
			TokenApprovalStore store = new TokenApprovalStore();
			store.setTokenStore(tokenStore);
			return store;
		}

		@Bean
		@Lazy
		@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
		public SparklrUserApprovalHandler userApprovalHandler() throws Exception {
			SparklrUserApprovalHandler handler = new SparklrUserApprovalHandler();
			handler.setApprovalStore(approvalStore());
			handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
			handler.setClientDetailsService(clientDetailsService);
			handler.setUseApprovalStore(true);
			return handler;
		}
		

	}
	

}
