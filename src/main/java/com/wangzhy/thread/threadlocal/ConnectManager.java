package com.wangzhy.thread.threadlocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author wangzhy
 * @date 2024年04月23日
 */
public class ConnectManager {

   // =================未使用 ThreadLocal ==================================

  private static Connection conn = null;

  /**
   * 1. 线程安全问题， 并发情况下，会创建多个 Connection 对象
   * 2. Connection 对象线程不安全，多线程下，会出现问题。
   * @return
   * @throws SQLException
   */
  public static Connection openConnection() throws SQLException {
    if(conn == null){
      conn = DriverManager.getConnection("xxxxx");
    }
    return conn;
  }

  public static void closeConnection() throws SQLException {
    if(conn != null){
      conn.close();
    }
  }
  // =================使用 ThreadLocal ==================================

  private static final ThreadLocal<Connection> dbConnLocal = new ThreadLocal<Connection>() {
    @Override
    protected Connection initialValue() {
      try {
        return DriverManager.getConnection("", "", "");
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return null;
    }
  };

  public Connection getConnection() {
    return dbConnLocal.get();
  }

}

class  Dao{

  /**
   * 服务器压力大，会频繁的创建连接。
   * @throws SQLException
   */
  public void insert() throws SQLException {
    ConnectManager manager = new ConnectManager();
    Connection conn = manager.openConnection();

    // do something ...
    manager.closeConnection();
  }
}