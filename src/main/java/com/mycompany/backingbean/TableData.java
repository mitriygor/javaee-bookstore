/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.backingbean;

import com.mycompany.beans.InventoryJpaController;
import com.mycompany.entities.Inventory;
import com.mycompany.entities.Reviews;
import com.mycompany.viewbean.PreRenderViewBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
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
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.persistence.criteria.Join;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

@Named
@SessionScoped
public class TableData implements Serializable {

    @Inject
    private InventoryJpaController inventoryJpaController;

    private static Inventory inventory;
    private String param;
    private PreRenderViewBean preRenderViewBean;

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager entityManager;

    public void setParam(String str) {
        param = str;
    }

    public String getParam() {
        return param;
    }

    /**
     *
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getInventories() throws IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.select(inventoryRoot);
        TypedQuery<Inventory> query = entityManager.createQuery(cq);
        Collection<Inventory> inventories = query.getResultList();
        return inventories;
    }
    
       /**
     *
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getLimitedInventoriesForFirstPage() throws IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.select(inventoryRoot);
        TypedQuery<Inventory> query = entityManager.createQuery(cq).setMaxResults(24);
        Collection<Inventory> inventories = query.getResultList();
        Collections.shuffle((List<?>) inventories);
        return inventories;
    }

    /**
     *
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getLatestInventories() throws IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.select(inventoryRoot);
        cq.orderBy(cb.desc(inventoryRoot.get("dateofentry")));
        TypedQuery<Inventory> query = entityManager.createQuery(cq).setMaxResults(4);
        Collection<Inventory> inventories = query.getResultList();
        Collections.shuffle((List<?>) inventories);
        return inventories;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getInventoriesByTitle(HttpServletRequest request)
            throws ServletException, IOException {
        Metamodel m = entityManager.getMetamodel();
        EntityType<Inventory> Inventory_ = m.entity(Inventory.class);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.where(cb.like(inventoryRoot.<String>get("title"), "%" + request.getParameter("req") + "%"));
        TypedQuery<Inventory> query = entityManager.createQuery(cq);
        Collection<Inventory> inventories = query.getResultList();
        return inventories;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getInventoriesByAuthor(HttpServletRequest request)
            throws ServletException, IOException {
        Metamodel m = entityManager.getMetamodel();
        EntityType<Inventory> Inventory_ = m.entity(Inventory.class);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.where(cb.like(inventoryRoot.<String>get("authors"), "%" + request.getParameter("req") + "%"));
        TypedQuery<Inventory> query = entityManager.createQuery(cq);
        Collection<Inventory> inventories = query.getResultList();
        return inventories;
    }
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getInventoriesByGenre(HttpServletRequest request)
            throws ServletException, IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.where(cb.equal(inventoryRoot.get("genre"), request.getParameter("genre")));
        TypedQuery<Inventory> query = entityManager.createQuery(cq);
        Collection<Inventory> inventories = query.getResultList();
        return inventories;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public Collection<Inventory> getLimitedInventoriesByGenre(String str)
            throws ServletException, IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.where(cb.equal(inventoryRoot.get("genre"), str));
        TypedQuery<Inventory> query = entityManager.createQuery(cq).setMaxResults(6);
        Collection<Inventory> inventories = query.getResultList();
        Collections.shuffle((List<?>) inventories);
        return inventories;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @return
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void setInventory(HttpServletRequest request)
            throws ServletException, IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.where(cb.equal(inventoryRoot.get("isbn"), request.getParameter("isbn")));
        TypedQuery<Inventory> query = entityManager.createQuery(cq);
        inventory = query.getSingleResult();
    }
    
        /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @return
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public Inventory getInventoryByIsbn(String isbn)
            throws ServletException, IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
        Root<Inventory> inventoryRoot = cq.from(Inventory.class);
        cq.where(cb.equal(inventoryRoot.get("isbn"), isbn));
        TypedQuery<Inventory> query = entityManager.createQuery(cq);
        Inventory inventoryByIsbn = query.getSingleResult();
        return inventoryByIsbn;
    }

    public Inventory getInventory() {
        return inventory;
    }

}
