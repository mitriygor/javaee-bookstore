/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.test;

import com.mycompany.beans.InventoryJpaController;
import com.mycompany.backingbean.TableData;
import com.mycompany.entities.Inventory;
import com.mycompany.beans.exceptions.RollbackFailureException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author mitriy
 */
@RunWith(Arquillian.class)
public class InventoryTest {

//    private Logger logger = Logger.getLogger(InventoryTest.class.getName());
    @Deployment
    public static WebArchive deploy() {

        // Use an alternative to the JUnit assert library called AssertJ
        // Need to reference MySQL driver as it is not part of either
        // embedded or remote
        final File[] dependencies = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("mysql:mysql-connector-java",
                        "org.assertj:assertj-core").withoutTransitivity()
                .asFile();

        // The webArchive is the special packaging of your project
        // so that only the test cases run on the server or embedded
        // container
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"))
                .addPackage(TableData.class.getPackage())
                .addPackage(RollbackFailureException.class.getPackage())
                .addPackage(Inventory.class.getPackage())
                .addPackage(InventoryJpaController.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/glassfish-resources.xml"), "glassfish-resources.xml")
                .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
                .addAsResource("bookstoreMySQL.sql")
                .addAsLibraries(dependencies);
        
        return webArchive;
    }

    @Inject
    private TableData tableData;

    @Inject
    private InventoryJpaController inventoryJpaController;

    @Resource(name = "java:app/jdbc/myBookStoreTwo")
    private DataSource ds;

    /**
     *
     * @throws SQLException
     * @throws java.io.IOException
     */
    @Test
    public void getInventories() throws SQLException, IOException {
        long t = System.nanoTime();
        List<Inventory> lfd = new ArrayList<>(tableData.getInventories());
        assertThat(lfd).hasSize(25);
        double seconds = (double) (System.nanoTime() - t) / 1000000000.0;
        System.out.println("getInventories: " + seconds + " seconds.");
    }
    
    /**
     *
     * @throws SQLException
     * @throws java.io.IOException
     */
    @Test
    public void getLimitedInventoriesForFirstPage() throws SQLException, IOException {
        long t = System.nanoTime();
        List<Inventory> lfd = new ArrayList<>(tableData.getLimitedInventoriesForFirstPage());
        assertThat(lfd).hasSize(24);
        double seconds = (double) (System.nanoTime() - t) / 1000000000.0;
        System.out.println("getLimitedInventoriesForFirstPage: " + seconds + " seconds.");
    }
    
     /**
     *
     * @throws SQLException
     * @throws java.io.IOException
     */
    @Test
    public void getLatestInventories() throws SQLException, IOException {
        long t = System.nanoTime();
        List<Inventory> lfd = new ArrayList<>(tableData.getLatestInventories());
        assertThat(lfd).hasSize(4);
        double seconds = (double) (System.nanoTime() - t) / 1000000000.0;
        System.out.println("getLatestInventories: " + seconds + " seconds.");
    }
    
//    getInventoriesByTitle
//    getInventoriesByAuthor
//    getInventoriesByGenre
//    setInventory
    
    
       /**
     *
     * @throws SQLException
     * @throws java.io.IOException
     */
    @Test
    public void getLimitedInventoriesByGenre() throws SQLException, IOException, ServletException {
        long t = System.nanoTime();
        List<Inventory> lfd = new ArrayList<>(tableData.getLimitedInventoriesByGenre("business"));
        assertThat(lfd).hasSize(6);
        double seconds = (double) (System.nanoTime() - t) / 1000000000.0;
        System.out.println("getLimitedInventoriesByGenre: " + seconds + " seconds.");
    }
    
      /**
     *
     * @throws SQLException
     * @throws java.io.IOException
     */
    @Test
    public void getInventoryByIsbn() throws SQLException, IOException, ServletException {
        long t = System.nanoTime();
        Inventory lfd = tableData.getInventoryByIsbn("9780307463746");
        assertNotNull(lfd);
        double seconds = (double) (System.nanoTime() - t) / 1000000000.0;
        System.out.println("getInventoryByIsbn: " + seconds + " seconds.");
    }
    
    
    

}
