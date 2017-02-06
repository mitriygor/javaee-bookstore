/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.backingbean;

import com.mycompany.beans.ClientsJpaController;
import com.mycompany.beans.ClientsJpaController1;
import com.mycompany.beans.InventoryJpaController;
import com.mycompany.entities.Clients;
import com.mycompany.entities.Inventory;
import com.mycompany.entities.Invoices;
import com.mycompany.entities.Reviews;
import java.io.IOException;
import java.io.PrintWriter;
import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.Join;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mitriy
 */
@Named
@SessionScoped
public class DetailsBackingBean implements Serializable {

    @Inject
    private ClientsJpaController1 clientsJpaController;

    @Inject
    private ClientBackingBean clientBackingBean;

    private Collection<Inventory> library;

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager entityManager;
    
    private  int count = 0;

    public  int getCount() throws ServletException, IOException {
        addBooksToLibrary();
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void addBooksToLibrary() throws ServletException, IOException {
        Clients client = clientBackingBean.getClient();
        if (client != null) {
            System.out.println("YES");
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            int id = (Integer) session.getAttribute("clientId");
            Clients loggedClient = clientsJpaController.findClients(id);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
            Root<Invoices> invoicesRoot = cq.from(Invoices.class);
            Join invoicesDetails = invoicesRoot.join("detailsCollection");
            cq.where(cb.equal(invoicesDetails.get("invoicenum").get("clientnum"), client));
            cq.multiselect(invoicesDetails.get("isbn"));
            TypedQuery<Inventory> query = entityManager.createQuery(cq);
            Collection<Inventory> inventories = query.getResultList();
            setLibrary(inventories);
            setCount(inventories.size());
        } else {
                    System.out.println("NoS");

        }
    }

    public  Collection<Inventory> getLibrary() {
        return library;
    }

    public void setLibrary(Collection<Inventory> library) {
        this.library = library;
    }

}
