/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.beans;

import com.mycompany.beans.exceptions.NonexistentEntityException;
import com.mycompany.beans.exceptions.RollbackFailureException;
import com.mycompany.entities.Details;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.entities.Invoices;
import com.mycompany.entities.Inventory;
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
public class DetailsJpaController implements Serializable {
    
    @Resource
    private UserTransaction utx = null;
    @PersistenceContext
    private EntityManager em;


    public void create(Details details) throws RollbackFailureException, Exception {
//        EntityManager em = null;
        try {
            utx.begin();
            Invoices invoicenum = details.getInvoicenum();
            if (invoicenum != null) {
                invoicenum = em.getReference(invoicenum.getClass(), invoicenum.getInvoicenum());
                details.setInvoicenum(invoicenum);
            }
            Inventory isbn = details.getIsbn();
            if (isbn != null) {
                isbn = em.getReference(isbn.getClass(), isbn.getIsbn());
                details.setIsbn(isbn);
            }
            em.persist(details);
            if (invoicenum != null) {
                invoicenum.getDetailsCollection().add(details);
                invoicenum = em.merge(invoicenum);
            }
            if (isbn != null) {
                isbn.getDetailsCollection().add(details);
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

    public void edit(Details details) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Details persistentDetails = em.find(Details.class, details.getId());
            Invoices invoicenumOld = persistentDetails.getInvoicenum();
            Invoices invoicenumNew = details.getInvoicenum();
            Inventory isbnOld = persistentDetails.getIsbn();
            Inventory isbnNew = details.getIsbn();
            if (invoicenumNew != null) {
                invoicenumNew = em.getReference(invoicenumNew.getClass(), invoicenumNew.getInvoicenum());
                details.setInvoicenum(invoicenumNew);
            }
            if (isbnNew != null) {
                isbnNew = em.getReference(isbnNew.getClass(), isbnNew.getIsbn());
                details.setIsbn(isbnNew);
            }
            details = em.merge(details);
            if (invoicenumOld != null && !invoicenumOld.equals(invoicenumNew)) {
                invoicenumOld.getDetailsCollection().remove(details);
                invoicenumOld = em.merge(invoicenumOld);
            }
            if (invoicenumNew != null && !invoicenumNew.equals(invoicenumOld)) {
                invoicenumNew.getDetailsCollection().add(details);
                invoicenumNew = em.merge(invoicenumNew);
            }
            if (isbnOld != null && !isbnOld.equals(isbnNew)) {
                isbnOld.getDetailsCollection().remove(details);
                isbnOld = em.merge(isbnOld);
            }
            if (isbnNew != null && !isbnNew.equals(isbnOld)) {
                isbnNew.getDetailsCollection().add(details);
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
                Integer id = details.getId();
                if (findDetails(id) == null) {
                    throw new NonexistentEntityException("The details with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Details details;
            try {
                details = em.getReference(Details.class, id);
                details.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The details with id " + id + " no longer exists.", enfe);
            }
            Invoices invoicenum = details.getInvoicenum();
            if (invoicenum != null) {
                invoicenum.getDetailsCollection().remove(details);
                invoicenum = em.merge(invoicenum);
            }
            Inventory isbn = details.getIsbn();
            if (isbn != null) {
                isbn.getDetailsCollection().remove(details);
                isbn = em.merge(isbn);
            }
            em.remove(details);
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

    public List<Details> findDetailsEntities() {
        return findDetailsEntities(true, -1, -1);
    }

    public List<Details> findDetailsEntities(int maxResults, int firstResult) {
        return findDetailsEntities(false, maxResults, firstResult);
    }

    private List<Details> findDetailsEntities(boolean all, int maxResults, int firstResult) {
              EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Details.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
      
    }

    public Details findDetails(Integer id) {
                EntityManager em = null;
            return em.find(Details.class, id);

    }

    public int getDetailsCount() {
            EntityManager em = null;
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Details> rt = cq.from(Details.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
       
    }
    
}
