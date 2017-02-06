/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.beans;

import com.mycompany.beans.exceptions.NonexistentEntityException;
import com.mycompany.beans.exceptions.RollbackFailureException;
import com.mycompany.entities.Clients;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.entities.Reviews;
import java.util.ArrayList;
import java.util.Collection;
import com.mycompany.entities.Invoices;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

/**
 *
 * @author mitriy
 */
@Named
@RequestScoped
public class ClientsJpaController1 implements Serializable {

    @Resource
    private UserTransaction utx = null;
    @PersistenceContext
    private EntityManager em;

    public void create(Clients clients) throws RollbackFailureException, Exception {
        if (clients.getReviewsCollection() == null) {
            clients.setReviewsCollection(new ArrayList<Reviews>());
        }
        if (clients.getInvoicesCollection() == null) {
            clients.setInvoicesCollection(new ArrayList<Invoices>());
        }
        try {
            utx.begin();
            Collection<Reviews> attachedReviewsCollection = new ArrayList<Reviews>();
            for (Reviews reviewsCollectionReviewsToAttach : clients.getReviewsCollection()) {
                reviewsCollectionReviewsToAttach = em.getReference(reviewsCollectionReviewsToAttach.getClass(), reviewsCollectionReviewsToAttach.getId());
                attachedReviewsCollection.add(reviewsCollectionReviewsToAttach);
            }
            clients.setReviewsCollection(attachedReviewsCollection);
            Collection<Invoices> attachedInvoicesCollection = new ArrayList<Invoices>();
            for (Invoices invoicesCollectionInvoicesToAttach : clients.getInvoicesCollection()) {
                invoicesCollectionInvoicesToAttach = em.getReference(invoicesCollectionInvoicesToAttach.getClass(), invoicesCollectionInvoicesToAttach.getInvoicenum());
                attachedInvoicesCollection.add(invoicesCollectionInvoicesToAttach);
            }
            clients.setInvoicesCollection(attachedInvoicesCollection);
            em.persist(clients);
            for (Reviews reviewsCollectionReviews : clients.getReviewsCollection()) {
                Clients oldClientnumOfReviewsCollectionReviews = reviewsCollectionReviews.getClientnum();
                reviewsCollectionReviews.setClientnum(clients);
                reviewsCollectionReviews = em.merge(reviewsCollectionReviews);
                if (oldClientnumOfReviewsCollectionReviews != null) {
                    oldClientnumOfReviewsCollectionReviews.getReviewsCollection().remove(reviewsCollectionReviews);
                    oldClientnumOfReviewsCollectionReviews = em.merge(oldClientnumOfReviewsCollectionReviews);
                }
            }
            for (Invoices invoicesCollectionInvoices : clients.getInvoicesCollection()) {
                Clients oldClientnumOfInvoicesCollectionInvoices = invoicesCollectionInvoices.getClientnum();
                invoicesCollectionInvoices.setClientnum(clients);
                invoicesCollectionInvoices = em.merge(invoicesCollectionInvoices);
                if (oldClientnumOfInvoicesCollectionInvoices != null) {
                    oldClientnumOfInvoicesCollectionInvoices.getInvoicesCollection().remove(invoicesCollectionInvoices);
                    oldClientnumOfInvoicesCollectionInvoices = em.merge(oldClientnumOfInvoicesCollectionInvoices);
                }
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

    public void edit(Clients clients) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Clients persistentClients = em.find(Clients.class, clients.getClientnum());
            Collection<Reviews> reviewsCollectionOld = persistentClients.getReviewsCollection();
            Collection<Reviews> reviewsCollectionNew = clients.getReviewsCollection();
            Collection<Invoices> invoicesCollectionOld = persistentClients.getInvoicesCollection();
            Collection<Invoices> invoicesCollectionNew = clients.getInvoicesCollection();
            Collection<Reviews> attachedReviewsCollectionNew = new ArrayList<Reviews>();
            for (Reviews reviewsCollectionNewReviewsToAttach : reviewsCollectionNew) {
                reviewsCollectionNewReviewsToAttach = em.getReference(reviewsCollectionNewReviewsToAttach.getClass(), reviewsCollectionNewReviewsToAttach.getId());
                attachedReviewsCollectionNew.add(reviewsCollectionNewReviewsToAttach);
            }
            reviewsCollectionNew = attachedReviewsCollectionNew;
            clients.setReviewsCollection(reviewsCollectionNew);
            Collection<Invoices> attachedInvoicesCollectionNew = new ArrayList<Invoices>();
            for (Invoices invoicesCollectionNewInvoicesToAttach : invoicesCollectionNew) {
                invoicesCollectionNewInvoicesToAttach = em.getReference(invoicesCollectionNewInvoicesToAttach.getClass(), invoicesCollectionNewInvoicesToAttach.getInvoicenum());
                attachedInvoicesCollectionNew.add(invoicesCollectionNewInvoicesToAttach);
            }
            invoicesCollectionNew = attachedInvoicesCollectionNew;
            clients.setInvoicesCollection(invoicesCollectionNew);
            clients = em.merge(clients);
            for (Reviews reviewsCollectionOldReviews : reviewsCollectionOld) {
                if (!reviewsCollectionNew.contains(reviewsCollectionOldReviews)) {
                    reviewsCollectionOldReviews.setClientnum(null);
                    reviewsCollectionOldReviews = em.merge(reviewsCollectionOldReviews);
                }
            }
            for (Reviews reviewsCollectionNewReviews : reviewsCollectionNew) {
                if (!reviewsCollectionOld.contains(reviewsCollectionNewReviews)) {
                    Clients oldClientnumOfReviewsCollectionNewReviews = reviewsCollectionNewReviews.getClientnum();
                    reviewsCollectionNewReviews.setClientnum(clients);
                    reviewsCollectionNewReviews = em.merge(reviewsCollectionNewReviews);
                    if (oldClientnumOfReviewsCollectionNewReviews != null && !oldClientnumOfReviewsCollectionNewReviews.equals(clients)) {
                        oldClientnumOfReviewsCollectionNewReviews.getReviewsCollection().remove(reviewsCollectionNewReviews);
                        oldClientnumOfReviewsCollectionNewReviews = em.merge(oldClientnumOfReviewsCollectionNewReviews);
                    }
                }
            }
            for (Invoices invoicesCollectionOldInvoices : invoicesCollectionOld) {
                if (!invoicesCollectionNew.contains(invoicesCollectionOldInvoices)) {
                    invoicesCollectionOldInvoices.setClientnum(null);
                    invoicesCollectionOldInvoices = em.merge(invoicesCollectionOldInvoices);
                }
            }
            for (Invoices invoicesCollectionNewInvoices : invoicesCollectionNew) {
                if (!invoicesCollectionOld.contains(invoicesCollectionNewInvoices)) {
                    Clients oldClientnumOfInvoicesCollectionNewInvoices = invoicesCollectionNewInvoices.getClientnum();
                    invoicesCollectionNewInvoices.setClientnum(clients);
                    invoicesCollectionNewInvoices = em.merge(invoicesCollectionNewInvoices);
                    if (oldClientnumOfInvoicesCollectionNewInvoices != null && !oldClientnumOfInvoicesCollectionNewInvoices.equals(clients)) {
                        oldClientnumOfInvoicesCollectionNewInvoices.getInvoicesCollection().remove(invoicesCollectionNewInvoices);
                        oldClientnumOfInvoicesCollectionNewInvoices = em.merge(oldClientnumOfInvoicesCollectionNewInvoices);
                    }
                }
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
                Integer id = clients.getClientnum();
                if (findClients(id) == null) {
                    throw new NonexistentEntityException("The clients with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        try {
            utx.begin();
            Clients clients;
            try {
                clients = em.getReference(Clients.class, id);
                clients.getClientnum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clients with id " + id + " no longer exists.", enfe);
            }
            Collection<Reviews> reviewsCollection = clients.getReviewsCollection();
            for (Reviews reviewsCollectionReviews : reviewsCollection) {
                reviewsCollectionReviews.setClientnum(null);
                reviewsCollectionReviews = em.merge(reviewsCollectionReviews);
            }
            Collection<Invoices> invoicesCollection = clients.getInvoicesCollection();
            for (Invoices invoicesCollectionInvoices : invoicesCollection) {
                invoicesCollectionInvoices.setClientnum(null);
                invoicesCollectionInvoices = em.merge(invoicesCollectionInvoices);
            }
            em.remove(clients);
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

    public List<Clients> findClientsEntities() {
        return findClientsEntities(true, -1, -1);
    }

    public List<Clients> findClientsEntities(int maxResults, int firstResult) {
        return findClientsEntities(false, maxResults, firstResult);
    }

    private List<Clients> findClientsEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Clients.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Clients findClients(Integer id) {
        return em.find(Clients.class, id);
    }
    
        /**
     * Find a record with the specified email and password
     *
     * @param username
     * @param password
     * @return
     */
    public Clients findClients(String email) {
        TypedQuery<Clients> query = em.createNamedQuery("Clients.findByEmail", Clients.class);
        query.setParameter("email", email);
        List<Clients> clients = query.getResultList();
        if (!clients.isEmpty()) {
            return clients.get(0);
        }
        return null;
    }

    public int getClientsCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Clients> rt = cq.from(Clients.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
