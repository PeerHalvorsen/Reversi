/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Reversi;

/**

 @author Peer-Anders
 */
@WebServlet(name = "ReversiServlet", urlPatterns =
{
    "/placeDisk"
})
public class ReversiServlet extends HttpServlet
{

    /**
     Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     methods.

     @param request servlet request
     @param response servlet response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        final HttpSession session = request.getSession();
        final Reversi game = (Reversi) session.getAttribute("game");

        String reset = request.getParameter("reset");
        String pass = request.getParameter("pass");
        String quit = request.getParameter("quit");
        
        if (reset != null) //if reset button was clicked
        {
            game.reset();
        }
        else if (pass != null) //if pass button was clicked
        {
            game.pass();
        }
        else if (quit != null)
        {
            game.quitGame(); //if quit button was clicked
        }
        else
        {
            String cellIndexStr = request.getParameter("cellIndex"); //pass cell index
            if (cellIndexStr != null && cellIndexStr.trim().matches("[0-9]*")) //make sure its not null and is numeric
            {
                int cellIndex = Integer.parseInt(cellIndexStr);
                if (!game.placeDisk(cellIndex)) //if move is invalid send message
                {
                    request.setAttribute("errMsg", "Invalid Move");
                }
            }
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     Handles the HTTP <code>GET</code> method.

     @param request servlet request
     @param response servlet response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     Handles the HTTP <code>POST</code> method.

     @param request servlet request
     @param response servlet response
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     Returns a short description of the servlet.

     @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}
