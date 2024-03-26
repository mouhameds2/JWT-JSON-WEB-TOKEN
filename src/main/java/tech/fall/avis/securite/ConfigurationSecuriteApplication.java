package tech.fall.avis.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class ConfigurationSecuriteApplication{
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFilter jwtFilter;
   // private final UserDetailsService userDetailsService;
    public ConfigurationSecuriteApplication(BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilter = jwtFilter;
      //  this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        int id = 0;
        return
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                authorize ->
                                        authorize
                                                .requestMatchers(POST,"utilisateur/inscription").permitAll()
                                                .requestMatchers(POST,"utilisateur/activation").permitAll()
                                                .requestMatchers(POST,"utilisateur/connexion").permitAll()
                                                .requestMatchers(POST,"utilisateur/modifier-mot-de-passe").permitAll()
                                                .requestMatchers(POST,"utilisateur/nouveau-mot-de-passe").permitAll()
                                                .requestMatchers(POST,"utilisateur/refresh-token").permitAll()
                                                .requestMatchers(GET,"/utilisateur/{id}").permitAll()
                                                .requestMatchers(GET,"/utilisateur").permitAll()
                                                .requestMatchers(GET,"/utilisateur/user").permitAll()

                                                .anyRequest().authenticated()
                        )//on ajoute une session pour l'authentification de l'utilsateur
                        .sessionManagement(httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                                )//on ajoute un filtre ici
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }
    //pour crypter les mots de passe en base de données
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder (){
        return new BCryptPasswordEncoder();
    }


     //AuthenticationManager qui gére la connexion qui s'appui sur le provider qui se connecte à la base de données
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

   // Nous permet d'acceder à la base de données
    @Bean
    public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }

    /*@Bean
    public UserDetailsService userDetailsService (){
        return new UtilisateurService();
    }

     */

}
