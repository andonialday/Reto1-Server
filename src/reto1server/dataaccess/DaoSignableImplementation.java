/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto1server.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import reto1libraries.exception.CredentialErrorException;
import reto1libraries.exception.DBConnectionException;
import reto1libraries.exception.LoginOnUseException;
import reto1libraries.logic.Signable;
import reto1libraries.object.User;

/**
 * The class to implements the methods of the Dao to connect to the BBDD
 *
 * @author Enaitz Izagirre
 */
public class DaoSignableImplementation implements Signable {

    private Connection con;
    private PreparedStatement stmt;
    private static final Pool pool = new Pool();
    private static final Logger LOGGER = Logger.getLogger("package.class");

    // SQL PreparedStatement String
    private final String SIGNIN = "CALL `login`(?, ?)";
    private final String SIGNUP = "CALL `register`(?, ?, ?, ?)";
    private final String CHECKLOGIN = "SELECT * from user WHERE Login = ?";

    /**
     * Method to make the sign in to the database
     *
     * @param user It recives the user to get user data
     * @return Returns The new user with the Result Set data
     * @throws DBConnectionException Error processing feedback from the Database
     * @throws CredentialErrorException Could not verify user credentials on the
     * Database
     */
    @Override
    public synchronized User signIn(User user) throws DBConnectionException, CredentialErrorException {

        LOGGER.info("DB SignIn procedure initiated");
        con = pool.getConnection();
        ResultSet rs = null;
        User usr = new User();
        try {
            stmt = con.prepareStatement(SIGNIN);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            rs = stmt.executeQuery();

            while (rs.next()) {
                usr.setId(rs.getInt("ID"));
                usr.setLogin(rs.getString("Login"));
                usr.setEmail(rs.getString("Email"));
                usr.setFullName(rs.getString("FullName"));
                usr.setPassword(rs.getString("Password"));
                usr.setLastPasswordChange(rs.getTimestamp("LastPasswordChange"));
            }
        } catch (SQLException e) {
            throw new CredentialErrorException("Could not verify user credentials on the Database");
        }
        pool.returnConnection(con);
        try {
            LOGGER.info("DB SignIn procedure ending");
            rs.close();
        } catch (SQLException ex) {
            throw new DBConnectionException("Error processing feedback from the Database");
        }
        return usr;
    }

    /**
     * Method to make the Sign Un to the Database
     *
     * @param user It recives the user to get user data
     * @return The new user with the Result Set data
     * @throws DBConnectionException Error processing feedback from the Database
     * @throws LoginOnUseException Could not Create user credentials on the
     * Database
     */
    @Override
    public synchronized User signUp(User user) throws DBConnectionException, LoginOnUseException {

        LOGGER.info("DB SignUp procedure initiated");
        ResultSet rs = null;
        User usr = new User();
        con = pool.getConnection();
        try {
            stmt = con.prepareStatement(CHECKLOGIN);
            stmt.setString(1, user.getLogin());
            LOGGER.info("Checking login availability");
            rs = stmt.executeQuery();
            while (rs.next()) {
                usr.setLogin(rs.getString("Login"));
            }
            if (usr.getLogin() == null) {
                rs = null;

                stmt = con.prepareStatement(SIGNUP);
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getFullName());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getPassword());

                LOGGER.info("Sending SignUp request to the DB");
                rs = stmt.executeQuery();

                while (rs.next()) {
                    usr.setId(rs.getInt("ID"));
                    usr.setLogin(rs.getString("Login"));
                    usr.setEmail(rs.getString("Email"));
                    usr.setFullName(rs.getString("FullName"));
                    //usr.setStatus((UserStatus) UserStatus.valueOf(rs.getString("UserStatus")));
                    //usr.setPrivilege((Privilege) Privilege.valueOf(rs.getString("UserPrivilege")));
                    usr.setPassword(rs.getString("Password"));
                    usr.setLastPasswordChange(rs.getTimestamp("LastPasswordChange"));
                }
            } else {
                throw new LoginOnUseException("Login on use on the DataBase");
            }
        } catch (SQLException ex) {
            throw new DBConnectionException("Could not Create user credentials on the Database");
        }
        pool.returnConnection(con);
        try {
            LOGGER.info("DB SignUp procedure ending");
            rs.close();
        } catch (SQLException ex) {
            throw new DBConnectionException("Error processing feedback from the Database");
        }

        return usr;
    }

}
