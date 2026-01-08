<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="page-content">
    <div class="analytics">
        <div class="card">
            <div class="card-head">
                <h2><fmt:formatNumber value="${monthlyRevenue}" type="number"/> VNĐ</h2>
                <span class="las la-wallet"></span>
            </div>
            <small>Doanh thu tháng hiện tại</small>
        </div>
    </div>

    <div class="records" style="margin-top: 2rem; padding: 1.5rem;">
        <h2>📈 Biểu đồ tăng trưởng doanh thu (7 ngày gần nhất)</h2>
        <div style="height: 450px;">
            <canvas id="revenueLineChart"></canvas>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const ctx = document.getElementById('revenueLineChart').getContext('2d');
    
    // Dữ liệu từ Map gửi từ Controller
    const labels = [];
    const data = [];
    
    <c:forEach var="entry" items="${dailyRevenueMap}">
        labels.push('${entry.key}');
        data.push(${entry.value});
    </c:forEach>

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu (VNĐ)',
                data: data,
                borderColor: '#22BAA0',
                backgroundColor: 'rgba(34, 186, 160, 0.1)',
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: { 
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) { return value.toLocaleString() + ' ₫'; }
                    }
                }
            }
        }
    });
</script>