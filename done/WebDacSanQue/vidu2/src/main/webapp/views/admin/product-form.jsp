<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>

<style>
/* 1. KHÔNG VIẾT GÌ VÀO CLASS .sidebar HAY .profile NỮA ĐỂ TRÁNH LỆCH */

/* 2. HỆ THỐNG CLASS RIÊNG BIỆT CHO FORM (Prefix: adm-form-) */
.adm-form-container {
	padding: 20px;
	background-color: transparent;
}

.adm-form-card {
	background: #ffffff;
	border-radius: 12px;
	box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
	border: none;
	overflow: hidden;
	margin-top: 10px;
}

.adm-form-header {
	background-color: #34495e; /* Màu dark header chuẩn */
	color: #ffffff;
	padding: 15px 25px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.adm-form-content {
	padding: 30px;
}

/* Layout chia 2 cột dùng Flexbox độc lập */
.adm-form-row {
	display: flex;
	flex-wrap: wrap;
	gap: 40px;
}

.adm-form-main-col {
	flex: 1;
	min-width: 350px;
}

.adm-form-side-col {
	width: 380px;
	padding-left: 30px;
	border-left: 1px solid #edf2f7;
}

/* Custom Input Styles */
.adm-form-group {
	margin-bottom: 20px;
}

.adm-form-label {
	display: block;
	font-weight: 600;
	margin-bottom: 8px;
	color: #2d3748;
	font-size: 0.95rem;
}

.adm-form-input {
	width: 100%;
	padding: 10px 15px;
	border: 1px solid #e2e8f0;
	border-radius: 6px;
	font-size: 1rem;
	transition: all 0.3s;
}

.adm-form-input:focus {
	border-color: #59b198;
	box-shadow: 0 0 0 3px rgba(89, 177, 152, 0.1);
	outline: none;
}

/* Image Preview Section */
.adm-img-box {
	width: 100%;
	height: 250px;
	background: #f7fafc;
	border: 2px dashed #cbd5e0;
	border-radius: 10px;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow: hidden;
	margin-bottom: 15px;
}

.adm-img-render {
	max-width: 100%;
	max-height: 100%;
	object-fit: contain;
}

/* Buttons */
.adm-btn-group {
	margin-top: 30px;
	padding-top: 20px;
	border-top: 1px solid #edf2f7;
	text-align: right;
}

.adm-btn-confirm {
	background-color: #59b198; /* Màu xanh chuẩn CMS */
	color: white;
	border: none;
	padding: 12px 30px;
	border-radius: 6px;
	font-weight: 700;
	cursor: pointer;
	box-shadow: 0 4px 6px rgba(89, 177, 152, 0.2);
}

.adm-btn-back {
	text-decoration: none;
	color: #718096;
	padding: 12px 25px;
	margin-right: 15px;
	font-weight: 600;
}
</style>

<div class="adm-form-container">
	<div class="adm-form-card">
		<div class="adm-form-header">
			<h5 style="margin: 0">
				<i class="fas fa-box-open me-2"></i> THÔNG TIN SẢN PHẨM
			</h5>
			<span style="font-size: 0.85rem; opacity: 0.9;"> CHẾ ĐỘ:
				${action == 'add' ? 'THÊM MỚI' : 'CẬP NHẬT'} </span>
		</div>

		<div class="adm-form-content">
			<form action="${pageContext.request.contextPath}/admin/product/save"
				method="post">
				<input type="hidden" name="id" value="${product.id}">

				<div class="adm-form-row">
					<div class="adm-form-main-col">
						<div class="adm-form-group">
							<label class="adm-form-label">Tên sản phẩm <span
								style="color: red">*</span></label> <input type="text" name="name"
								class="adm-form-input" value="${product.name}" required>
						</div>

						<div style="display: flex; gap: 20px;">
							<div class="adm-form-group" style="flex: 1">
								<label class="adm-form-label">Danh mục <span
									style="color: red">*</span></label> <select name="cateID"
									class="adm-form-input" required>
									<option value="">-- Chọn danh mục --</option>
									<c:forEach items="${categories}" var="cat">
										<option value="${cat.id}"
											${product.cateID == cat.id ? 'selected' : ''}>
											${cat.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="adm-form-group" style="flex: 1">
								<label class="adm-form-label">Giá bán (VNĐ) <span
									style="color: red">*</span></label>
								<%-- Sử dụng fmt:formatNumber để ép giá về số nguyên không có phần thập phân --%>
								<fmt:formatNumber value="${product.price}" pattern="#"
									var="formattedPrice" />
								<input type="number" name="price" class="adm-form-input"
									style="color: #2dcc70; font-weight: bold"
									value="${formattedPrice}" step="1" required>
							</div>
						</div>

						<div class="adm-form-group">
							<label class="adm-form-label">Tiêu đề ngắn (Title)</label> <input
								type="text" name="title" class="adm-form-input"
								value="${product.title}">
						</div>

						<div class="adm-form-group">
							<label class="adm-form-label">Mô tả chi tiết</label>
							<textarea name="description" class="adm-form-input" rows="6">${product.description}</textarea>
						</div>
					</div>

					<div class="adm-form-side-col">
						<div class="adm-form-group">
							<label class="adm-form-label">Số lượng trong kho <span
								style="color: red">*</span></label> <input type="number" name="stock"
								class="adm-form-input" value="${product.stock}" required>
						</div>

						<div class="adm-form-group">
							<label class="adm-form-label">Hình ảnh minh họa</label>
							<div class="adm-img-box">
								<img id="adm-preview-tag"
									src="${not empty product.image ? product.image : 'https://via.placeholder.com/400x300?text=No+Image'}"
									class="adm-img-render">
							</div>
							<input type="text" id="adm-url-trigger" name="image"
								class="adm-form-input" value="${product.image}"
								placeholder="Dán link URL ảnh vào đây...">
						</div>
					</div>
				</div>

				<div class="adm-btn-group">
					<a href="${pageContext.request.contextPath}/admin/manageProduct"
						class="adm-btn-back">HỦY BỎ</a>
					<button type="submit" class="adm-btn-confirm">
						<i class="fas fa-save me-2"></i> XÁC NHẬN LƯU SẢN PHẨM
					</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
    (function() {
        const urlField = document.getElementById('adm-url-trigger');
        const imgDisplay = document.getElementById('adm-preview-tag');
        
        if (urlField && imgDisplay) {
            urlField.addEventListener('input', function() {
                const val = this.value.trim();
                imgDisplay.src = val ? val : 'https://via.placeholder.com/400x300?text=No+Image';
            });
            
            imgDisplay.onerror = function() {
                this.src = 'https://via.placeholder.com/400x300?text=Loi+Link+Anh';
            };
        }
    })();
</script>