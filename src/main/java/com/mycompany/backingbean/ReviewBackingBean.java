/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.backingbean;

import com.mycompany.beans.ReviewsJpaController;
import com.mycompany.backingbean.TableData;
import com.mycompany.backingbean.ClientBackingBean;
import com.mycompany.entities.Inventory;
import com.mycompany.entities.Clients;
import com.mycompany.entities.Reviews;
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
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.Join;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.servlet.http.HttpSession;

@Named
@SessionScoped
public class ReviewBackingBean implements Serializable {

    @Inject
    private ReviewsJpaController reviewsJpaController;

    @Inject
    private TableData tableData;

    @Inject
    private ClientBackingBean clientBackingBean;

    private Reviews review = new Reviews();
    private Collection<Object[]> reviews;

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     *
     * @return
     */
    public Reviews getReview() {
        return review;
    }

    /**
     * Save the current person to the db
     *
     * @return
     * @throws Exception
     */
    public void createReview(HttpServletRequest request)
            throws ServletException, IOException, Exception {
        Clients client = clientBackingBean.getClient();
        Inventory book = tableData.getInventory();
        review.setIsbn(book);
        review.setClientnum(client);
        review.setDateofreview(new Date());
        review.setApprovalstatus(true);
        reviewsJpaController.create(review);
        setReviews(book.getIsbn()); 
        review = new Reviews();

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
    public void setReviews(HttpServletRequest request) throws ServletException, IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Clients> clientsRoot = cq.from(Clients.class);
        Join clientsReviews = clientsRoot.join("reviewsCollection");

        cq.where(cb.equal(clientsReviews.get("isbn").get("isbn"), request.getParameter("isbn")));
        cq.multiselect(clientsRoot.get("firstname"), clientsRoot.get("lastname"), clientsReviews.get("rating"), clientsReviews.get("reviewtext"));

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        reviews = query.getResultList();
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
    public void setReviews(String isbn) throws ServletException, IOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Clients> clientsRoot = cq.from(Clients.class);
        Join clientsReviews = clientsRoot.join("reviewsCollection");

        cq.where(cb.equal(clientsReviews.get("isbn").get("isbn"), isbn));
        cq.multiselect(clientsRoot.get("firstname"), clientsRoot.get("lastname"), clientsReviews.get("rating"), clientsReviews.get("reviewtext"));

        TypedQuery<Object[]> query = entityManager.createQuery(cq);
        reviews = query.getResultList();
    }

    public Collection<Object[]> getReviews() {
        return reviews;
    }

}
