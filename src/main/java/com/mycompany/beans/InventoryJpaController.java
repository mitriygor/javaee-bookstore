/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.beans;

import com.mycompany.beans.exceptions.NonexistentEntityException;
import com.mycompany.beans.exceptions.PreexistingEntityException;
import com.mycompany.beans.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.entities.Reviews;
import java.util.ArrayList;
import java.util.Collection;
import com.mycompany.entities.Details;
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
//    private Collection<Inventory> inventories;
@Named
@RequestScoped
public class InventoryJpaController implements Serializable {
    
    @Resource
    private UserTransaction utx = null;
    @PersistenceContext
    private EntityManager em;


    public void create(Inventory inventory) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (inventory.getReviewsCollection() == null) {
            inventory.setReviewsCollection(new ArrayList<Reviews>());
        }
        if (inventory.getDetailsCollection() == null) {
            inventory.setDetailsCollection(new ArrayList<Details>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            Collection<Reviews> attachedReviewsCollection = new ArrayList<Reviews>();
            for (Reviews reviewsCollectionReviewsToAttach : inventory.getReviewsCollection()) {
                reviewsCollectionReviewsToAttach = em.getReference(reviewsCollectionReviewsToAttach.getClass(), reviewsCollectionReviewsToAttach.getId());
                attachedReviewsCollection.add(reviewsCollectionReviewsToAttach);
            }
            inventory.setReviewsCollection(attachedReviewsCollection);
            Collection<Details> attachedDetailsCollection = new ArrayList<Details>();
            for (Details detailsCollectionDetailsToAttach : inventory.getDetailsCollection()) {
                detailsCollectionDetailsToAttach = em.getReference(detailsCollectionDetailsToAttach.getClass(), detailsCollectionDetailsToAttach.getId());
                attachedDetailsCollection.add(detailsCollectionDetailsToAttach);
            }
            inventory.setDetailsCollection(attachedDetailsCollection);
            em.persist(inventory);
            for (Reviews reviewsCollectionReviews : inventory.getReviewsCollection()) {
                Inventory oldIsbnOfReviewsCollectionReviews = reviewsCollectionReviews.getIsbn();
                reviewsCollectionReviews.setIsbn(inventory);
                reviewsCollectionReviews = em.merge(reviewsCollectionReviews);
                if (oldIsbnOfReviewsCollectionReviews != null) {
                    oldIsbnOfReviewsCollectionReviews.getReviewsCollection().remove(reviewsCollectionReviews);
                    oldIsbnOfReviewsCollectionReviews = em.merge(oldIsbnOfReviewsCollectionReviews);
                }
            }
            for (Details detailsCollectionDetails : inventory.getDetailsCollection()) {
                Inventory oldIsbnOfDetailsCollectionDetails = detailsCollectionDetails.getIsbn();
                detailsCollectionDetails.setIsbn(inventory);
                detailsCollectionDetails = em.merge(detailsCollectionDetails);
                if (oldIsbnOfDetailsCollectionDetails != null) {
                    oldIsbnOfDetailsCollectionDetails.getDetailsCollection().remove(detailsCollectionDetails);
                    oldIsbnOfDetailsCollectionDetails = em.merge(oldIsbnOfDetailsCollectionDetails);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findInventory(inventory.getIsbn()) != null) {
                throw new PreexistingEntityException("Inventory " + inventory + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(Inventory inventory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Inventory persistentInventory = em.find(Inventory.class, inventory.getIsbn());
            Collection<Reviews> reviewsCollectionOld = persistentInventory.getReviewsCollection();
            Collection<Reviews> reviewsCollectionNew = inventory.getReviewsCollection();
            Collection<Details> detailsCollectionOld = persistentInventory.getDetailsCollection();
            Collection<Details> detailsCollectionNew = inventory.getDetailsCollection();
            Collection<Reviews> attachedReviewsCollectionNew = new ArrayList<Reviews>();
            for (Reviews reviewsCollectionNewReviewsToAttach : reviewsCollectionNew) {
                reviewsCollectionNewReviewsToAttach = em.getReference(reviewsCollectionNewReviewsToAttach.getClass(), reviewsCollectionNewReviewsToAttach.getId());
                attachedReviewsCollectionNew.add(reviewsCollectionNewReviewsToAttach);
            }
            reviewsCollectionNew = attachedReviewsCollectionNew;
            inventory.setReviewsCollection(reviewsCollectionNew);
            Collection<Details> attachedDetailsCollectionNew = new ArrayList<Details>();
            for (Details detailsCollectionNewDetailsToAttach : detailsCollectionNew) {
                detailsCollectionNewDetailsToAttach = em.getReference(detailsCollectionNewDetailsToAttach.getClass(), detailsCollectionNewDetailsToAttach.getId());
                attachedDetailsCollectionNew.add(detailsCollectionNewDetailsToAttach);
            }
            detailsCollectionNew = attachedDetailsCollectionNew;
            inventory.setDetailsCollection(detailsCollectionNew);
            inventory = em.merge(inventory);
            for (Reviews reviewsCollectionOldReviews : reviewsCollectionOld) {
                if (!reviewsCollectionNew.contains(reviewsCollectionOldReviews)) {
                    reviewsCollectionOldReviews.setIsbn(null);
                    reviewsCollectionOldReviews = em.merge(reviewsCollectionOldReviews);
                }
            }
            for (Reviews reviewsCollectionNewReviews : reviewsCollectionNew) {
                if (!reviewsCollectionOld.contains(reviewsCollectionNewReviews)) {
                    Inventory oldIsbnOfReviewsCollectionNewReviews = reviewsCollectionNewReviews.getIsbn();
                    reviewsCollectionNewReviews.setIsbn(inventory);
                    reviewsCollectionNewReviews = em.merge(reviewsCollectionNewReviews);
                    if (oldIsbnOfReviewsCollectionNewReviews != null && !oldIsbnOfReviewsCollectionNewReviews.equals(inventory)) {
                        oldIsbnOfReviewsCollectionNewReviews.getReviewsCollection().remove(reviewsCollectionNewReviews);
                        oldIsbnOfReviewsCollectionNewReviews = em.merge(oldIsbnOfReviewsCollectionNewReviews);
                    }
                }
            }
            for (Details detailsCollectionOldDetails : detailsCollectionOld) {
                if (!detailsCollectionNew.contains(detailsCollectionOldDetails)) {
                    detailsCollectionOldDetails.setIsbn(null);
                    detailsCollectionOldDetails = em.merge(detailsCollectionOldDetails);
                }
            }
            for (Details detailsCollectionNewDetails : detailsCollectionNew) {
                if (!detailsCollectionOld.contains(detailsCollectionNewDetails)) {
                    Inventory oldIsbnOfDetailsCollectionNewDetails = detailsCollectionNewDetails.getIsbn();
                    detailsCollectionNewDetails.setIsbn(inventory);
                    detailsCollectionNewDetails = em.merge(detailsCollectionNewDetails);
                    if (oldIsbnOfDetailsCollectionNewDetails != null && !oldIsbnOfDetailsCollectionNewDetails.equals(inventory)) {
                        oldIsbnOfDetailsCollectionNewDetails.getDetailsCollection().remove(detailsCollectionNewDetails);
                        oldIsbnOfDetailsCollectionNewDetails = em.merge(oldIsbnOfDetailsCollectionNewDetails);
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
                String id = inventory.getIsbn();
                if (findInventory(id) == null) {
                    throw new NonexistentEntityException("The inventory with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            Inventory inventory;
            try {
                inventory = em.getReference(Inventory.class, id);
                inventory.getIsbn();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventory with id " + id + " no longer exists.", enfe);
            }
            Collection<Reviews> reviewsCollection = inventory.getReviewsCollection();
            for (Reviews reviewsCollectionReviews : reviewsCollection) {
                reviewsCollectionReviews.setIsbn(null);
                reviewsCollectionReviews = em.merge(reviewsCollectionReviews);
            }
            Collection<Details> detailsCollection = inventory.getDetailsCollection();
            for (Details detailsCollectionDetails : detailsCollection) {
                detailsCollectionDetails.setIsbn(null);
                detailsCollectionDetails = em.merge(detailsCollectionDetails);
            }
            em.remove(inventory);
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

    public List<Inventory> findInventoryEntities() {
        return findInventoryEntities(true, -1, -1);
    }

    public List<Inventory> findInventoryEntities(int maxResults, int firstResult) {
        return findInventoryEntities(false, maxResults, firstResult);
    }

    private List<Inventory> findInventoryEntities(boolean all, int maxResults, int firstResult) {
                EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventory.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
    }

    public Inventory findInventory(String id) {
                EntityManager em = null;

            return em.find(Inventory.class, id);
        
    }

    public int getInventoryCount() {
                EntityManager em = null;

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventory> rt = cq.from(Inventory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        
    }
    
}
