/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.backingbean;

import com.mycompany.beans.ClientsJpaController;
import com.mycompany.beans.ClientsJpaController1;
import com.mycompany.beans.DetailsJpaController;
import com.mycompany.beans.InventoryJpaController;
import com.mycompany.beans.InvoicesJpaController;
import com.mycompany.entities.Clients;
import com.mycompany.entities.Details;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.servlet.http.HttpSession;

@Named
@SessionScoped
public class InvoiceBackingBean implements Serializable {

    private String counter;
    private static int count = 0;
    private static BigDecimal totalCost = new BigDecimal(0);

    private static List<Inventory> inventories = new ArrayList<>();

    @Inject
    private InventoryJpaController inventoryJpaController;
    private Inventory inventory;

    @Inject
    private ClientsJpaController1 clientsJpaController;
    @Inject
    private DetailsJpaController detailsJpaController;
    @Inject
    private InvoicesJpaController invoicesJpaController;

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager entityManager;

    private String isbn = "";

    @Inject
    private ClientBackingBean clientBackingBean;
    
    private String ccname;

    public String getCcname() {
        return ccname;
    }

    public void setCcname(String ccname) {
        this.ccname = ccname;
    }

    public String getCcnunmber() {
        return ccnunmber;
    }

    public void setCcnunmber(String ccnunmber) {
        this.ccnunmber = ccnunmber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCcyear() {
        return ccyear;
    }

    public void setCcyear(String ccyear) {
        this.ccyear = ccyear;
    }

    public String getCcmonth() {
        return ccmonth;
    }

    public void setCcmonth(String ccmonth) {
        this.ccmonth = ccmonth;
    }
    private String ccnunmber;
    private String cvv;
    private String ccyear;
    private String ccmonth;
    
    

    public String geIsbn() {
        return isbn;
    }

    public void setIsbn(String newIsbn) {
        isbn = newIsbn;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @return
     * @throws IOException if an I/O error occurs
     */
    public List<Inventory> getInventories() throws IOException {
        return inventories;
    }

    /**
     *
     * @param book
     * @return
     * @throws IOException
     */
    public static String addInventoryToCart(Inventory book) throws IOException {
        String bookIsbn = book.getIsbn();
        for (Inventory inventory : inventories) {
            if (bookIsbn.equals(inventory.getIsbn())) {
                return "cart";
            }
        }
        inventories.add(book);
        setCount();
        return "cart";
    }

    public static String removeInventoryFromCart(Inventory book) throws IOException {
        Iterator<Inventory> books = inventories.iterator();
        while (books.hasNext()) {
            Inventory inv = books.next();
            if (inv.getIsbn().equals(book.getIsbn())) {
                books.remove();
            }
        }
        setCount();
        return "cart";
    }

    public void removeInventory(Inventory book) throws IOException {
        inventories.remove(book);
    }

    public static void setCount() {
        count = inventories.size();
    }

    public int getCount() {
        return count;
    }

    public void calcTotalCost() {
        BigDecimal calc = new BigDecimal(0);
        for (Inventory inventory : inventories) {
            calc = calc.add(inventory.getCost());
        }
        setTotalCost(calc);
    }

    public static BigDecimal getTotalCost() {
        return totalCost;
    }

    public String buy() throws ServletException, IOException, Exception {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        int id = (Integer) session.getAttribute("clientId");
        Clients client = clientsJpaController.findClients(id);

        if (client != null && getInventories().size() > 0) {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Invoices> cq = cb.createQuery(Invoices.class);
            Root<Invoices> invoicesRoot = cq.from(Invoices.class);

            for (Inventory inventory : inventories) {
                BigDecimal gstRate = new BigDecimal(0.05);
                gstRate = inventory.getCost().multiply(gstRate);
                Invoices invoice = new Invoices();
                Details detail = new Details();
                invoice.setDateofsale(new Date());
                invoice.setClientnum(client);
                invoice.setNetprice(inventory.getCost());
                invoice.setGstrate(gstRate);
                invoice.setTotalprice(gstRate.add(inventory.getCost()));

                invoicesJpaController.create(invoice);

                cq.select(invoicesRoot);
                cq.orderBy(cb.desc(invoicesRoot.get("invoicenum")));
                TypedQuery<Invoices> query = entityManager.createQuery(cq);
                Invoices invoices = (Invoices) query.setMaxResults(1).getResultList().get(0);
                if (invoices != null && invoices.getInvoicenum() != null) {
                    System.out.println("getInvoicenum");
                    System.out.println(invoices.getInvoicenum());
                    detail.setInvoicenum(invoices);
                    detail.setIsbn(inventory);
                    detail.setBookprice(inventory.getCost());
                    detailsJpaController.create(detail);
                }
            }

            count = 0;
            totalCost = new BigDecimal(0);
            inventories = new ArrayList<>();
        }
        return "library";
    }

    public static void setTotalCost(BigDecimal totalCost) {
        InvoiceBackingBean.totalCost = totalCost;
    }
    
    public void validateCcName(FacesContext fc, UIComponent c, Object value) {
        if (((String) value).length() < 3) {
            throw new ValidatorException(new FacesMessage(
                    "Name on credit card is too short"));
        }
    }
    
    public void validateCcNumber(FacesContext fc, UIComponent c, Object value) {
        if (((String) value).length() != 16) {
            throw new ValidatorException(new FacesMessage(
                    "Credit card number is not valid"));
        }
    }
    
    public void validateCvv(FacesContext fc, UIComponent c, Object value) {
        if (((String) value).length() != 3) {
            throw new ValidatorException(new FacesMessage(
                    "CVV is not valid"));
        }
    }
}
