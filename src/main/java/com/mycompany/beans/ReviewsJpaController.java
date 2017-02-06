/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.beans;

import com.mycompany.beans.exceptions.NonexistentEntityException;
import com.mycompany.beans.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.entities.Clients;
import com.mycompany.entities.Inventory;
import com.mycompany.entities.Reviews;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author mitriy
 */
@Named
@RequestScoped
public class ReviewsJpaController implements Serializable {
    
     @Resource
    private UserTransaction utx = null;
    @PersistenceContext
    private EntityManager em;

    public void create(Reviews reviews) throws RollbackFailureException, Exception {
//        EntityManager em = null;
        try {
            utx.begin();
            Clients clientnum = reviews.getClientnum();
            if (clientnum != null) {
                clientnum = em.getReference(clientnum.getClass(), clientnum.getClientnum());
                reviews.setClientnum(clientnum);
            }
            Inventory isbn = reviews.getIsbn();
            if (isbn != null) {
                isbn = em.getReference(isbn.getClass(), isbn.getIsbn());
                reviews.setIsbn(isbn);
            }
            em.persist(reviews);
            if (clientnum != null) {
                clientnum.getReviewsCollection().add(reviews);
                clientnum = em.merge(clientnum);
            }
            if (isbn != null) {
                isbn.getReviewsCollection().add(reviews);
                isbn = em.merge(isbn);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public void edit(Reviews reviews) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Reviews persistentReviews = em.find(Reviews.class, reviews.getId());
            Clients clientnumOld = persistentReviews.getClientnum();
            Clients clientnumNew = reviews.getClientnum();
            Inventory isbnOld = persistentReviews.getIsbn();
            Inventory isbnNew = reviews.getIsbn();
            if (clientnumNew != null) {
                clientnumNew = em.getReference(clientnumNew.getClass(), clientnumNew.getClientnum());
                reviews.setClientnum(clientnumNew);
            }
            if (isbnNew != null) {
                isbnNew = em.getReference(isbnNew.getClass(), isbnNew.getIsbn());
                reviews.setIsbn(isbnNew);
            }
            reviews = em.merge(reviews);
            if (clientnumOld != null && !clientnumOld.equals(clientnumNew)) {
                clientnumOld.getReviewsCollection().remove(reviews);
                clientnumOld = em.merge(clientnumOld);
            }
            if (clientnumNew != null && !clientnumNew.equals(clientnumOld)) {
                clientnumNew.getReviewsCollection().add(reviews);
                clientnumNew = em.merge(clientnumNew);
            }
            if (isbnOld != null && !isbnOld.equals(isbnNew)) {
                isbnOld.getReviewsCollection().remove(reviews);
                isbnOld = em.merge(isbnOld);
            }
            if (isbnNew != null && !isbnNew.equals(isbnOld)) {
                isbnNew.getReviewsCollection().add(reviews);
                isbnNew = em.merge(isbnNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reviews.getId();
                if (findReviews(id) == null) {
                    throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Reviews reviews;
            try {
                reviews = em.getReference(Reviews.class, id);
                reviews.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.", enfe);
            }
            Clients clientnum = reviews.getClientnum();
            if (clientnum != null) {
                clientnum.getReviewsCollection().remove(reviews);
                clientnum = em.merge(clientnum);
            }
            Inventory isbn = reviews.getIsbn();
            if (isbn != null) {
                isbn.getReviewsCollection().remove(reviews);
                isbn = em.merge(isbn);
            }
            em.remove(reviews);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public List<Reviews> findReviewsEntities() {
        return findReviewsEntities(true, -1, -1);
    }

    public List<Reviews> findReviewsEntities(int maxResults, int firstResult) {
        return findReviewsEntities(false, maxResults, firstResult);
    }

    private List<Reviews> findReviewsEntities(boolean all, int maxResults, int firstResult) {
                EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reviews.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
    }

    public Reviews findReviews(Integer id) {
        EntityManager em = null;

            return em.find(Reviews.class, id);

    }

    public int getReviewsCount() {
        EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reviews> rt = cq.from(Reviews.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();

    }
    
}
