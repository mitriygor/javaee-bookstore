/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mitriy
 */
@Entity
@Table(name = "INVENTORY", catalog = "BOOKSTORE", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findByIsbn", query = "SELECT i FROM Inventory i WHERE i.isbn = :isbn"),
    @NamedQuery(name = "Inventory.findByDateofentry", query = "SELECT i FROM Inventory i WHERE i.dateofentry = :dateofentry"),
    @NamedQuery(name = "Inventory.findByTitle", query = "SELECT i FROM Inventory i WHERE i.title = :title"),
    @NamedQuery(name = "Inventory.findBySubtitle", query = "SELECT i FROM Inventory i WHERE i.subtitle = :subtitle"),
    @NamedQuery(name = "Inventory.findByAuthors", query = "SELECT i FROM Inventory i WHERE i.authors = :authors"),
    @NamedQuery(name = "Inventory.findByPublisher", query = "SELECT i FROM Inventory i WHERE i.publisher = :publisher"),
    @NamedQuery(name = "Inventory.findByDateofpublication", query = "SELECT i FROM Inventory i WHERE i.dateofpublication = :dateofpublication"),
    @NamedQuery(name = "Inventory.findByPages", query = "SELECT i FROM Inventory i WHERE i.pages = :pages"),
    @NamedQuery(name = "Inventory.findByGenre", query = "SELECT i FROM Inventory i WHERE i.genre = :genre"),
    @NamedQuery(name = "Inventory.findByImage", query = "SELECT i FROM Inventory i WHERE i.image = :image"),
    @NamedQuery(name = "Inventory.findByCost", query = "SELECT i FROM Inventory i WHERE i.cost = :cost"),
    @NamedQuery(name = "Inventory.findByList", query = "SELECT i FROM Inventory i WHERE i.list = :list"),
    @NamedQuery(name = "Inventory.findByRemovalstatus", query = "SELECT i FROM Inventory i WHERE i.removalstatus = :removalstatus")})
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "ISBN")
    private String isbn;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATEOFENTRY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofentry;
    @Size(max = 75)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 75)
    @Column(name = "SUBTITLE")
    private String subtitle;
    @Size(max = 225)
    @Column(name = "AUTHORS")
    private String authors;
    @Size(max = 75)
    @Column(name = "PUBLISHER")
    private String publisher;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATEOFPUBLICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofpublication;
    @Column(name = "PAGES")
    private Integer pages;
    @Size(max = 75)
    @Column(name = "GENRE")
    private String genre;
    @Size(max = 75)
    @Column(name = "IMAGE")
    private String image;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "LIST")
    private BigDecimal list;
    @Column(name = "REMOVALSTATUS")
    private Boolean removalstatus;
    @OneToMany(mappedBy = "isbn")
    private Collection<Reviews> reviewsCollection;
    @OneToMany(mappedBy = "isbn")
    private Collection<Details> detailsCollection;

    public Inventory() {
    }

    public Inventory(String isbn) {
        this.isbn = isbn;
    }

    public Inventory(String isbn, Date dateofentry, Date dateofpublication) {
        this.isbn = isbn;
        this.dateofentry = dateofentry;
        this.dateofpublication = dateofpublication;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getDateofentry() {
        return dateofentry;
    }

    public void setDateofentry(Date dateofentry) {
        this.dateofentry = dateofentry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getDateofpublication() {
        return dateofpublication;
    }

    public void setDateofpublication(Date dateofpublication) {
        this.dateofpublication = dateofpublication;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getList() {
        return list;
    }

    public void setList(BigDecimal list) {
        this.list = list;
    }

    public Boolean getRemovalstatus() {
        return removalstatus;
    }

    public void setRemovalstatus(Boolean removalstatus) {
        this.removalstatus = removalstatus;
    }

    @XmlTransient
    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    @XmlTransient
    public Collection<Details> getDetailsCollection() {
        return detailsCollection;
    }

    public void setDetailsCollection(Collection<Details> detailsCollection) {
        this.detailsCollection = detailsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventory)) {
            return false;
        }
        Inventory other = (Inventory) object;
        if ((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entities.Inventory[ isbn=" + isbn + " ]";
    }
    
}
