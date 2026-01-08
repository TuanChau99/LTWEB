package vn.iotstar.daos;

import java.util.List;
import java.util.Map;

import vn.iotstar.entity.Cart;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderDetail;
import vn.iotstar.models.OrderModel;
import vn.iotstar.models.UserModel;

public interface OrderDAO {
    // Chỉ khai báo tên hàm, không viết code xử lý bên trong
    List<Order> getAllOrders();
    
    void updateStatus(int orderId, int status);
    
    List<OrderDetail> getOrderDetailByOrderId(int orderId);
    
    List<Order> getOrdersByUserId(int userId);
    
    int insertOrder(UserModel u, Cart cart, String address);
    Order getOrderById(int orderId);
    double getTotalRevenueThisMonth();
    Map<String, Double> getRevenueLast7Days();
    List<OrderModel> getRecentOrders(int limit, String keyword);
    int countPendingOrders();
    List<OrderModel> findByUserId(int userId);
    void deleteOrder(int orderId);
}