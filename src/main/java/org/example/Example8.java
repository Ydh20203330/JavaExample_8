package org.example;

import java.sql.*;

import java.util.*;

public class Example8 {

    public static Connection makeConnection() {
        String url = "jdbc:mysql://localhost:3306/3330db_01?characterEncoding=UTF-8 & serverTimezone=UTC";

        String id = "root";   String password = "2603";
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("드라이버 적재 성공.");
            con = DriverManager.getConnection(url, id, password);
            System.out.println("데이터베이스 연결 성공");


        } catch (ClassNotFoundException e){
            System.out.println("드라이버를 찾을 수 없습니다.");
        } catch (SQLException e) {
            System.out.println("연결에 실패하였습니다.");
            e.printStackTrace();
        }
        return con;
    }

    static void book_add(Connection con) throws SQLException{
        int id = 0;
        String title, publisher, author;
        Scanner sc = new Scanner(System.in);
        Statement stmt;
        PreparedStatement myStmt = null;
        ResultSet rs;

        System.out.println("\n책 추가 기능\n");
        stmt= con.createStatement();

        rs = stmt.executeQuery("SELECT id FROM bookdb");
        while(rs.next()){
            id ++;
        }

        myStmt = con.prepareStatement("insert into bookdb (id, title, publisher, author) values (?, ?, ?, ?)");

        System.out.print("추가할 책의 이름은? : ");
        title = sc.nextLine();

        System.out.print("추가할 책의 출판사는? : ");
        publisher = sc.nextLine();

        System.out.print("추가할 책의 작가는? : ");
        author = sc.nextLine();

        myStmt.setInt(1,id);
        myStmt.setString(2, title);
        myStmt.setString(3, publisher);
        myStmt.setString(4, author);
        myStmt.executeUpdate();
    }
    static void book_delete(Connection con) throws SQLException{
        int id = 0;
        Scanner sc = new Scanner(System.in);
        PreparedStatement myStmt = null;

        myStmt = con.prepareStatement("delete from bookdb where id = ?");

        System.out.println("\n책 삭제 기능\n");
        System.out.print("삭제할 책 번호는? : ");
        id = sc.nextInt();

        myStmt.setInt(1, id);
        myStmt.executeUpdate();
    }

    static void book_adjust(Connection con) throws SQLException{
        String attribute, before_value, after_value;
        Scanner sc = new Scanner(System.in);
        PreparedStatement myStmt = null;

        System.out.println("\n책 수정 기능\n");
        System.out.print("수정할 속성은? (title, publisher, author) : ");
        attribute = sc.nextLine();

        System.out.print("속성의 현재 값은? : ");
        before_value = sc.nextLine();

        System.out.print("속성의 바뀐 값은? : ");
        after_value = sc.nextLine();

        String updateQuery =  "update bookdb set " + attribute + "= ? where " + attribute + " = ?";
        myStmt = con.prepareStatement(updateQuery);

        myStmt.setString(1, after_value);
        myStmt.setString(2, before_value);

        myStmt.executeUpdate();
    }

    static void book_check(Connection con) throws SQLException{
        System.out.println("\n책 조회 기능\n");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM bookdb");
        while (rs.next()){
            String id = rs.getString("id");
            String title = rs.getString("title");
            String publisher = rs.getString("publisher");
            String author = rs.getString("author");
            System.out.println("id: "+id+", title: "+title+", publisher: "+publisher + ", author: "+author);
        }
        System.out.println();
    }

    public static void main(String[] args) throws SQLException {
        Connection con = makeConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM bookdb");
        Boolean checkExit = true;
        int checkSelect = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n데이터베이스 정보");
        while (rs.next()){
            String id = rs.getString("id");
            String title = rs.getString("title");
            String publisher = rs.getString("publisher");
            String author = rs.getString("author");
            System.out.println("id: "+id+", title: "+title+", publisher: "+publisher + ", author: "+author);
        }

        while(checkExit){
            System.out.println("----------------");
            System.out.println("1: 책 추가");
            System.out.println("2: 책 삭제");
            System.out.println("3: 책 수정");
            System.out.println("4: 책 조회");
            System.out.println("5: 종료");
            System.out.println("----------------");
            System.out.print("어떤 기능을 수행하시겠습니까?: ");
            checkSelect = sc.nextInt();
            if (checkSelect == 1){
                book_add(con);
            }
            else if (checkSelect == 2){
                book_delete(con);
            }
            else if (checkSelect == 3){
                book_adjust(con);
            }
            else if (checkSelect == 4){
                book_check(con);
            }
            else if (checkSelect == 5){
                System.out.println("\n종료...");
                checkExit = false;
            }

            checkSelect = 0;
        }

    }

}