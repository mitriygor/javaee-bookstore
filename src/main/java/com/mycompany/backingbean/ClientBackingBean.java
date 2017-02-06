/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.backingbean;

import javax.inject.Inject;

import com.mycompany.beans.ClientsJpaController;
import com.mycompany.beans.ClientsJpaController1;
import com.mycompany.viewbean.PreRenderViewBean;
import com.mycompany.entities.Clients;
import com.mycompany.entities.Details;
import com.mycompany.entities.Inventory;
import com.mycompany.entities.Invoices;
import com.mycompany.entities.Reviews;
import com.mycompany.util.MessagesUtil;
import java.io.IOException;

import javax.enterprise.context.RequestScoped;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

/**
 *
 * @author mitriy
 */
@Named
@SessionScoped
public class ClientBackingBean implements Serializable {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Inject
    private ClientsJpaController1 clientsJpaController;

    private static Clients client;

    private String email = new String();
    private String title = new String();

    private String firstname = new String();
    private String lastname = new String();
    private String companyname = new String();
    private String address1 = new String();
    private String address2 = new String();
    private String city = new String();
    private String province = new String();
    private String country = new String();
    private String postalcode = new String();
    private String telhome = new String();
    private String telcel = new String();
    private String password = new String();
    private String confirmPassword = new String();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getTelhome() {
        return telhome;
    }

    public void setTelhome(String telhome) {
        this.telhome = telhome;
    }

    public String getTelcel() {
        return telcel;
    }

    public void setTelcel(String telcel) {
        this.telcel = telcel;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager entityManager;

    // Default logger is java.util.logging
    private static final Logger log = Logger.getLogger("ClientJpaController.class");

    private String logemail = new String();
    private String logpassword = new String();

    public String getLogemail() {
        return logemail;
    }

    public void setLogemail(String email) {
        this.logemail = email;
    }

    public String getLogpassword() {
        return logpassword;
    }

    public void setLogpassword(String password) {
        this.logpassword = password;
    }

    /**
     * Action
     */
    public String login() {
        // Get Session object so that the status of the individual who just logged in
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("clientId", null);

        // Example of logging
        log.log(Level.INFO, "Entering login method email is {0}", new Object[]{logemail});

        // Used to contain that will be displayed on the login form after Login button is pressed
        FacesMessage message;

        // Is there a client with these credentials
        if (getLogemail() != null && getLogemail().length() > 4 && getLogpassword() != null && getLogpassword().length() > 5) {
            client = clientsJpaController.findClients(getLogemail());

            // There is a client so login was successful
            if (client != null) {
                if (client.getPassword().equals(getLogpassword())) {
                    message = MessagesUtil.getMessage("com.mycompany.bundles.messages", "welcome", new Object[]{logemail});
                    message.setSeverity(FacesMessage.SEVERITY_INFO);
                    session.setAttribute("clientId", client.getClientnum());
//                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "index";
                } else {
                    message = MessagesUtil.getMessage("com.mycompany.bundles.messages", "loginerror", new Object[]{logemail});
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
//                    FacesContext.getCurrentInstance().addMessage(null, message);
                    return "login";
                }
            } else {
                // Unsuccessful login
                // MessagesUtil simplifies creating localized messages
                message = MessagesUtil.getMessage("com.mycompany.bundles.messages", "loginerror", new Object[]{logemail});
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
//                FacesContext.getCurrentInstance().addMessage(null, message);
                return "login";
            }
        } else {
            return "login";
        }
    }

    /**
     * Client created if it does not exist.
     *
     * @return
     */
    public Clients getClient() {
        return client;
    }

    /**
     * Client created if it does not exist.
     *
     * @return
     */
    public void setClient(int id) {
        if (client == null) {
            client = clientsJpaController.findClients(id);
        }
    }

    /**
     * Save the current person to the db
     *
     * @return
     * @throws Exception
     */
    public String createClient() throws Exception {
        Clients newClient = new Clients();
        newClient.setEmail(getEmail());
        newClient.setPassword(getPassword());
        newClient.setTitle(getTitle());
        newClient.setLastname(getLastname());
        newClient.setFirstname(getFirstname());
        newClient.setCompanyname(getCompanyname());
        newClient.setAddress1(getAddress1());
        newClient.setAddress2(getAddress2());
        newClient.setCity(getCity());
        newClient.setProvince(getProvince());
        newClient.setCountry(getCountry());
        newClient.setPostalcode(getPostalcode());
        newClient.setTelhome(getTelhome());
        newClient.setTelcel(getTelcel());
        clientsJpaController.create(newClient);
        return "login";
    }

    /**
     * Save the current person to the db
     *
     * @return
     * @throws Exception
     */
    public String getClientName() throws Exception {
        String clientName = "";
        if (client != null) {
            clientName = client.getFirstname();
        }
        return clientName;
    }

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        PreRenderViewBean preRenderViewBean = new PreRenderViewBean();

        session.setAttribute("clientId", null);
        ec.invalidateSession();
        client = null;
        preRenderViewBean.setLogged(false);
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
    }

    /**
     * *validation methods***
     */
    /**
     * Method called to validate the input. The validation rule is that there
     * cannot be an underscore in the string
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateEmail(FacesContext fc, UIComponent c, Object value) {

        if (((String) value).length() < 5) {
            throw new ValidatorException(new FacesMessage(
                    "Email is not valid"));
        }

        if (!((String) value).contains("@")) {
            throw new ValidatorException(new FacesMessage(
                    "There is supposed to be '@'"));
        }

        if (!((String) value).contains(".")) {
            throw new ValidatorException(new FacesMessage(
                    "Lack of domain name"));
        }

        Matcher matcher = EMAIL_REGEX.matcher((String) value);
        if (!matcher.find()) {
            throw new ValidatorException(new FacesMessage(
                    "Email is not valid"));
        }

    }

    /**
     * Method called to validate the input. The validation rule is that there
     * cannot be an underscore in the string
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validatePassword(FacesContext fc, UIComponent c, Object value) {
        if (((String) value).length() < 6) {
            throw new ValidatorException(new FacesMessage(
                    "Password should have six symbols at least"));
        }
    }

    /**
     * Method called to validate the input. The validation rule is that there
     * cannot be an underscore in the string
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateConfirmPassword(FacesContext fc, UIComponent c, Object value) {
        if (!((String) value).equals(getPassword())) {
            throw new ValidatorException(new FacesMessage(
                    "Password confirmation failed"));
        }
    }

    /**
     * Method called to validate the input. The validation rule is that there
     * cannot be an underscore in the string
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateFirstname(FacesContext fc, UIComponent c, Object value) {
        if (((String) value).length() < 2) {
            throw new ValidatorException(new FacesMessage(
                    "At least two letters"));
        }
    }

    /**
     * Method called to validate the input. The validation rule is that there
     * cannot be an underscore in the string
     *
     * @param fc
     * @param c
     * @param value
     */
    public void validateLastname(FacesContext fc, UIComponent c, Object value) {

        if (((String) value).length() < 2) {
            throw new ValidatorException(new FacesMessage(
                    "At least two letters"));
        }

    }
}
