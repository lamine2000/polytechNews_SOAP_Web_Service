package db.dao;
import java.sql.*;

public class UserDAO {

    //connection function
    public static Connection getConnection() throws SQLException{
        Connection connect;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/mglsi_news","java","java");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mglsi_news","java","java");
    }

    //this function return admin's token basing on his id
    public String getToken(int id) throws SQLException{
        PreparedStatement sql = getConnection().prepareStatement("Select tokenValue from user WHERE  id=?");
        sql.setInt(1,id);
        ResultSet token = sql.executeQuery();
        token.next();
        return token.getString("tokenValue");
    }

    //This function return login and password's user
    public String getUserPassword(String login) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("Select password from user WHERE  login=?");
        sql.setString(1,login);
        ResultSet infos = sql.executeQuery();
        infos.next();
        return infos.getString("password");
    }

    //this function retrieves the list of users
    public ResultSet getUsers() throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("Select * from user");
        ResultSet userList = sql.executeQuery();
        return userList;
    }

    //this function add user in the database
    public void addUser(String login, String password, String type) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("INSERT INTO user(login,password,type) VALUES (?,?,?)");
        sql.setString(1,login);
        sql.setString(2,password);
        sql.setString(3,type);
        sql.executeUpdate();
    }

    //update function depending on user's login
    public void updateUser(String login, String password, String type,int id) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("UPDATE user SET login=?, password=?, type=? WHERE id=? ");
        sql.setString(1,login);
        sql.setString(2,password);
        sql.setString(3,type);
        sql.setInt(4,id);
        sql.executeUpdate();
    }

    //delete function depending on user's login
    public void deleteUser(int id) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("DELETE FROM user WHERE id=?");
        sql.setInt(1,id);
        sql.executeUpdate();
    }

}
