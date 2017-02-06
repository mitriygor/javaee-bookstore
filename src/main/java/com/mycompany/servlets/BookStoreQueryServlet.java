/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.servlets;

import com.mycompany.entities.Clients;
import com.mycompany.entities.Details;
import com.mycompany.entities.Inventory;
import com.mycompany.entities.Invoices;
import com.mycompany.entities.Reviews;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.annotation.WebServlet;
import javax.transaction.UserTransaction;

/**
 *
 * @author mitriy
 */
public class BookStoreQueryServlet extends HttpServlet {
    
    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BookStoreQueryServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println(request.getParameter("isbn"));
            
////        Criteria for Select all records
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
            Root<Inventory> inventory = cq.from(Inventory.class);
            cq.where(cb.equal(inventory.get("isbn"), request.getParameter("isbn")));

//            cq.select(inventory);
            TypedQuery<Inventory> query = em.createQuery(cq);
            Collection<Inventory> inventories = query.getResultList();
            showTable(inventories, out);
//            
//            
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void showTable(Collection<Inventory> inventories, java.io.PrintWriter out) {
         out.println("<P ALIGN='center'><TABLE BORDER=1>");

        // table header
        out.println("<TR>");
        out.println("<TH>Inventory Name</TH>");
        out.println("<TH>Author</TH>");
        out.println("</TR>");

        Iterator<Inventory> iterator = inventories.iterator();
        while (iterator.hasNext()) {
            Inventory inventory = iterator.next();
            out.println("<TR>");
            out.println("<TD>" + inventory.getTitle() + "</TD>");
            out.println("<TD>" + inventory.getAuthors() + "</TD>");
            out.println("</TR>");
        }
        out.println("</TABLE></P>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
