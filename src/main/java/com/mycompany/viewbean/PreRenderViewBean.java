/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.viewbean;

import com.mycompany.entities.Inventory;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mitriy
 */
@Named
@RequestScoped
public class PreRenderViewBean implements Serializable {

    private static String genre;
    private static Boolean logged = false;

    public String getGenre() {
        return genre;
    }

    public static void setGenre(String genre) {
        PreRenderViewBean.genre = genre;
    }

    public void setLogged(Boolean log) {
        logged = log;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void checkSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session.getAttribute("clientId") != null) {
            setLogged(true);

        } else {
            setLogged(false);
        }
    }
    
    
    public void isLogged() throws IOException {
        
        if (!getLogged()) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/faces/login.xhtml");
        }
    }

    public void checkCookies() {
        FacesContext context = FacesContext.getCurrentInstance();
        Object my_cookie = context.getExternalContext().getRequestCookieMap().get("genre");
        if (my_cookie != null) {
            setGenre(((Cookie) my_cookie).getValue());
        }
    }

//    public void writeCookie(ComponentSystemEvent event) {
//        String id = (String) event.getComponent().getAttributes().get("myid");
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.getExternalContext().addResponseCookie("genre", id, null);
//    }
//
//    public void writeCookie(String str) {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.getExternalContext().addResponseCookie("genre", str, null);
//    }

    public void writeCookie(Inventory inv) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().addResponseCookie("genre", inv.getGenre(), null);
    }

//    public void writeCookie(HttpServletRequest request) {
//        String str = request.getParameter("genre");
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.getExternalContext().addResponseCookie("genre", str, null);
//    }

}
