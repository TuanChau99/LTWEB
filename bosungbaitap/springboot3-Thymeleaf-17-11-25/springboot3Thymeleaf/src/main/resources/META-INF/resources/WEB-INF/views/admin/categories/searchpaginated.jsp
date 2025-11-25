<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>




	<section class="row">

		<div class="col mt-4">

			<div class="card">

				<div class="card-header">List Category</div>

				<div class="card-body">

					<!-- Hiển thông báo -->

					<c:if test="${message != null}">

						<div class="alert alert-primary" role="alert">

							<i>${message}</i>

						</div>

					</c:if>

					<!-- Hêt thông báo -->

					<div class="row mt-2 mb-2">

						<div class="col-md-6">

							<form action="/admin/categories/searchpaginated">

								<div class="input-group">

									<input type="text" class="form-control ml-2" name="name"
										id="name" placeholder="Nhập từ khóa để tìm" />

									<button class="btn btn-outline-primary ml-2">Search</button>



								</div>

							</form>

						</div>

						<div class="col-md-6">

							<div class="float-right">

								<a class="btn btn-outline-success" href="/admin/categories/add">Add
									New Category</a>

							</div>

						</div>

					</div>

					<c:if test="${!categoryPage.hasContent()}">

						<div class="row">

							<div class="col">

								<div class="alert alert-danger" role="alert">No Category

									Found</div>

							</div>

						</div>

					</c:if>

					<c:if test="${categoryPage.hasContent()}">

						<table class="table table-striped table-inverse table-responsive">



							<thead class="thead-inverse">

								<tr>

									<th>Category ID</th>

									<th>Category Name</th>

									<th>Action</th>

								</tr>

							</thead>

							<tbody>

								<c:forEach items="${categoryPage.content}" var="category">

									<tr>

										<td scope="row">${category.categoryId}</td>

										<td>${category.name}</td>

										<td><a
											href="/admin/categories/view/${category.categoryId}"
											class="btn btn-outline-info"><i class="fas fa-info"></i></a>

											<a href="/admin/categories/edit/${category.categoryId}"
											class="btn btn-outline-warning"><i class="fas fa-edit"></i></a>

											<a data-id="${category.categoryId}"
											data-name="${category.name}"
											onclick="showconfirmation(this.getAttribute('data-id'),this.getAttribute('data-name'))"
											class="btn btn-outline-danger"><i class="fa fa-trash"></i></a>

										</td>

									</tr>

								</c:forEach>

							</tbody>



						</table>

					</c:if>

					<script type="text/javascript">
						function showconfirmation(id, name) {

							$('#productName').text(name);

							$('#yesOption').attr('href',
									'/admin/categories/delete/' + id);

							$('#confirmationId').modal('show');

						}
					</script>

					<!-- Modal -->

					<div class="modal fade" id="confirmationId" tabindex="-1"
						aria-labelledby="confirmationLabel" aria-hidden="true">

						<div class="modal-dialog">

							<div class="modal-content">

								<div class="modal-header">

									<h5 class="modal-title" id="confirmationLabel">Confirmation</h5>

									<button type="button" class="btn-close" data-bs-dismiss="modal"
										aria-label="Close"></button>

								</div>

								<div class="modal-body">

									Bạn có muốn xóa "<span id="productName"></span>"?

								</div>

								<div class="modal-footer">

									<a id="yesOption" class="btn btn-primary">Yes</a>

									<button type="button" class="btn btn-secondary"
										data-bs-dismiss="modal">Close</button>


								</div>

							</div>

						</div>

					</div>

					<!-- Modal -->

				</div>

				<div class="card-footer text-muted">

					<div class="row">

						<div class="col-3">

							<form action="">

								<div class="mb-3 input-group float-left">

									<label for="size" class="mr-2">Page size:</label> <select
										class="form-select ml-2" name="size" aria-label="size"
										id="size" onchange="this.form.submit()">

										<option ${categoryPage.size == 3 ? 'selected':''} value="3">3</option>

										<option ${categoryPage.size == 5 ? 'selected':''} value="5">5</option>

										<option ${categoryPage.size == 10 ? 'selected':''} value="10">10</option>

										<option ${categoryPage.size == 15 ? 'selected':''} value="15">15</option>

										<option ${categoryPage.size == 20 ? 'selected':''} value="20">20</option>

									</select>

								</div>

							</form>

						</div>

						<div class="col-7">

							<c:if test="${categoryPage.totalPages > 0}">

								<nav aria-label="Page navigation">


									<ul class="pagination justify-content-center">

										<li
											class="${1 == categoryPage.number + 1 ? 'page-item active' : 'page-item'}">

											<a
											href="<c:url value='/admin/categories/searchpaginated?name=${name}&size=${categoryPage.size}&page=${1}'/>"
											class="page-link">First</a>

										</li>

										<c:forEach items="${pageNumbers}" var="pageNumber">

											<c:if test="${categoryPage.totalPages > 1}">

												<li
													class="${pageNumber == categoryPage.number + 1 ? 'page-item active' : 'page-item'}">

													<a
													href="<c:url value='/admin/categories/searchpaginated?name=${name}&size=${categoryPage.size}&page=${pageNumber}'/>"
													class="page-link">${pageNumber}</a>

												</li>

											</c:if>

										</c:forEach>

										<li
											class="${categoryPage.totalPages == categoryPage.number + 1 ? 'page-item active' : 'page-item'}">

											<a
											href="<c:url value='/admin/categories/searchpaginated?name=${name}&size=${categoryPage.size}&page=${categoryPage.totalPages}'/>"
											class="page-link">Last</a>

										</li>

									</ul>


								</nav>

							</c:if>

						</div>

					</div>


				</div>

			</div>



		</div>

	</section>
