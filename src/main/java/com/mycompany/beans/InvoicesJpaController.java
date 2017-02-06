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
import com.mycompany.entities.Details;
import com.mycompany.entities.Invoices;
import java.util.ArrayList;
import java.util.Collection;
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
public class InvoicesJpaController implements Serializable {
    
    @Resource
    private UserTransaction utx = null;
    @PersistenceContext
    private EntityManager em;

    public void create(Invoices invoices) throws RollbackFailureException, Exception {
        if (invoices.getDetailsCollection() == null) {
            invoices.setDetailsCollection(new ArrayList<Details>());
        }
//        EntityManager em = null;
        try {
            utx.begin();
            Clients clientnum = invoices.getClientnum();
            if (clientnum != null) {
                clientnum = em.getReference(clientnum.getClass(), clientnum.getClientnum());
                invoices.setClientnum(clientnum);
            }
            Collection<Details> attachedDetailsCollection = new ArrayList<Details>();
            for (Details detailsCollectionDetailsToAttach : invoices.getDetailsCollection()) {
                detailsCollectionDetailsToAttach = em.getReference(detailsCollectionDetailsToAttach.getClass(), detailsCollectionDetailsToAttach.getId());
                attachedDetailsCollection.add(detailsCollectionDetailsToAttach);
            }
            invoices.setDetailsCollection(attachedDetailsCollection);
            em.persist(invoices);
            if (clientnum != null) {
                clientnum.getInvoicesCollection().add(invoices);
                clientnum = em.merge(clientnum);
            }
            for (Details detailsCollectionDetails : invoices.getDetailsCollection()) {
                Invoices oldInvoicenumOfDetailsCollectionDetails = detailsCollectionDetails.getInvoicenum();
                detailsCollectionDetails.setInvoicenum(invoices);
                detailsCollectionDetails = em.merge(detailsCollectionDetails);
                if (oldInvoicenumOfDetailsCollectionDetails != null) {
                    oldInvoicenumOfDetailsCollectionDetails.getDetailsCollection().remove(detailsCollectionDetails);
                    oldInvoicenumOfDetailsCollectionDetails = em.merge(oldInvoicenumOfDetailsCollectionDetails);
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

    public void edit(Invoices invoices) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Invoices persistentInvoices = em.find(Invoices.class, invoices.getInvoicenum());
            Clients clientnumOld = persistentInvoices.getClientnum();
            Clients clientnumNew = invoices.getClientnum();
            Collection<Details> detailsCollectionOld = persistentInvoices.getDetailsCollection();
            Collection<Details> detailsCollectionNew = invoices.getDetailsCollection();
            if (clientnumNew != null) {
                clientnumNew = em.getReference(clientnumNew.getClass(), clientnumNew.getClientnum());
                invoices.setClientnum(clientnumNew);
            }
            Collection<Details> attachedDetailsCollectionNew = new ArrayList<Details>();
            for (Details detailsCollectionNewDetailsToAttach : detailsCollectionNew) {
                detailsCollectionNewDetailsToAttach = em.getReference(detailsCollectionNewDetailsToAttach.getClass(), detailsCollectionNewDetailsToAttach.getId());
                attachedDetailsCollectionNew.add(detailsCollectionNewDetailsToAttach);
            }
            detailsCollectionNew = attachedDetailsCollectionNew;
            invoices.setDetailsCollection(detailsCollectionNew);
            invoices = em.merge(invoices);
            if (clientnumOld != null && !clientnumOld.equals(clientnumNew)) {
                clientnumOld.getInvoicesCollection().remove(invoices);
                clientnumOld = em.merge(clientnumOld);
            }
            if (clientnumNew != null && !clientnumNew.equals(clientnumOld)) {
                clientnumNew.getInvoicesCollection().add(invoices);
                clientnumNew = em.merge(clientnumNew);
            }
            for (Details detailsCollectionOldDetails : detailsCollectionOld) {
                if (!detailsCollectionNew.contains(detailsCollectionOldDetails)) {
                    detailsCollectionOldDetails.setInvoicenum(null);
                    detailsCollectionOldDetails = em.merge(detailsCollectionOldDetails);
                }
            }
            for (Details detailsCollectionNewDetails : detailsCollectionNew) {
                if (!detailsCollectionOld.contains(detailsCollectionNewDetails)) {
                    Invoices oldInvoicenumOfDetailsCollectionNewDetails = detailsCollectionNewDetails.getInvoicenum();
                    detailsCollectionNewDetails.setInvoicenum(invoices);
                    detailsCollectionNewDetails = em.merge(detailsCollectionNewDetails);
                    if (oldInvoicenumOfDetailsCollectionNewDetails != null && !oldInvoicenumOfDetailsCollectionNewDetails.equals(invoices)) {
                        oldInvoicenumOfDetailsCollectionNewDetails.getDetailsCollection().remove(detailsCollectionNewDetails);
                        oldInvoicenumOfDetailsCollectionNewDetails = em.merge(oldInvoicenumOfDetailsCollectionNewDetails);
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
                Integer id = invoices.getInvoicenum();
                if (findInvoices(id) == null) {
                    throw new NonexistentEntityException("The invoices with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Invoices invoices;
            try {
                invoices = em.getReference(Invoices.class, id);
                invoices.getInvoicenum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The invoices with id " + id + " no longer exists.", enfe);
            }
            Clients clientnum = invoices.getClientnum();
            if (clientnum != null) {
                clientnum.getInvoicesCollection().remove(invoices);
                clientnum = em.merge(clientnum);
            }
            Collection<Details> detailsCollection = invoices.getDetailsCollection();
            for (Details detailsCollectionDetails : detailsCollection) {
                detailsCollectionDetails.setInvoicenum(null);
                detailsCollectionDetails = em.merge(detailsCollectionDetails);
            }
            em.remove(invoices);
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

    public List<Invoices> findInvoicesEntities() {
        return findInvoicesEntities(true, -1, -1);
    }

    public List<Invoices> findInvoicesEntities(int maxResults, int firstResult) {
        return findInvoicesEntities(false, maxResults, firstResult);
    }

    private List<Invoices> findInvoicesEntities(boolean all, int maxResults, int firstResult) {
                EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Invoices.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
   
    }

    public Invoices findInvoices(Integer id) {
                EntityManager em = null;

            return em.find(Invoices.class, id);

    }

    public int getInvoicesCount() {
                EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Invoices> rt = cq.from(Invoices.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();

    }
    
}
