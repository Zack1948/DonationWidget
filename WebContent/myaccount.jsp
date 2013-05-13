<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.*"%>
<%@page import="com.paypal.donation.models.*"%>
<%@page import="com.paypal.donation.services.*"%>
<%@page import="com.paypal.donation.utils.Constants"%>
<% 
	java.util.List<Object[]> widgetList = null;
	try{
		widgetList = WidgetService.getInstance().findWidgets(request);
	}catch(Exception exc){
		response.sendRedirect("/error");
	}
	int noOfWidgets = 0;
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title><%=ApplicationProperties.getProperty(Constants.WIDGET_TITLE) %></title>
	
	<link href="assets/css/bootstrap.css" type="text/css" rel="stylesheet" media="screen">
	<link rel="shortcut icon" href="assets/images/pp_favicon_x.ico">
	
	<style type="text/css">
		html,body{height:100%}
		#wrap{min-height:100%;height:auto!important;height:100%;margin:0 auto;background-color:#f5f5f5}
	</style>
</head>
<body>
<div id="wrap">
	<!-- Header -->
	<%@ include file="header.html"%>
	<!-- End Header -->
	
	<div style="background-color: #F5F5F5;">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="text-right span10 offset1 content">
					<small><span>Hi, <i><%=session.getAttribute("FNAME") %>!</i><br />
						<span>Not <i><%=session.getAttribute("FNAME") %></i>? <a href="ac?method=logout">Sign
								Out</a></span></span>
					</small>
				</div>
			</div>

			<!-- Create Widget Button-->
			<div class="row-fluid">
				<div class="span10 offset1">
					<a href="admin"
						class="btn btn-primary btn-large btn-block">Create New Widget</a>
				</div>
			</div>
			
			<!-- Table for list of widgets -->
			<div class="row-fluid">
				<div class="span10 offset1">
					<h1 style="color: #0079C1;" class="text-center">Widgets Dashboard</h1>
					<!-- Error Content -->
					<div class="text-error alert alert-error" id="errorContent" style="display: none;"></div>
					<div>
					<% if(widgetList == null || widgetList.size() == 0) { %>
						<p class="lead content">No widgets found!</p>
					<% } else { %>
						<table class="table table-striped table-condensed">
							<tr>
								<th>#</th>
								<th>Title</th>
								<th>Goal Amount</th>
								<th>Amount Collected</th>
								<th>Operations</th>
							</tr>
							<%	for (Object[] widget: widgetList) { 
								noOfWidgets++;
							%>
							<tr> 
								<td><%=noOfWidgets%></td>
								<td>
									<a href="admin?id=<%=((String)widget[0])%>"><%=URLDecoder.decode((String)widget[1], "UTF-8")%></a>
								</td>
								<td><%=(Double)widget[2]%></td>
								<td><%=(Double)widget[3]%></td>
								<td>
									<a class="btn btn-mini" id="showHTML" title="Get HTML" data-toggle="modal" href="#getHTML<%=((String)widget[0])%>">
										<i class="icon-download-alt"></i> </a>
									<a class="btn btn-mini" href="p?i=<%=((String)widget[0]) %>" target="_blank" 
										title="Print Preview"><i class="icon-print"></i> </a>									
									<a class="btn btn-mini" title="Delete" data-toggle="modal" href="#delete<%=((String)widget[0])%>">
										<i class="icon-trash"></i> </a>
									<a class="btn btn-mini" id="qrcode" title="Show QR Code" data-toggle="modal" href="#qrcode<%=((String)widget[0])%>">
											<i class="icon-qrcode"></i> </a>
								</td>
								<!-- Modal Window for getting the HTML code -->
								<div id="getHTML<%=((String)widget[0])%>" class="modal hide fade in" style="display: none;">  
									<div class="modal-body text-center content">  
										<p>Paste this HTML in your web-page to start accepting Donations</p>
										 <textarea style="resize: none;" rows="5" class="input-block-level">
<iframe src="<%=ApplicationProperties.getProperty(Constants.APP_URL)%>/r?i=<%=((String)widget[0])%>" id="wiframe" style="width: 205px; height: 320px; border: 0px; overflow: hidden;"></iframe>
										 </textarea>
									</div>  
									<div class="modal-footer">  
										<a href="#" class="btn" data-dismiss="modal">Close</a>  
									</div>  
								</div>
								<!-- Modal Window for DELETE operation -->
								<div id="delete<%=((String)widget[0])%>" class="modal hide fade in" style="display: none;">  
									<div class="modal-body">  
										<%=ApplicationProperties.getProperty(Constants.DELETE_WIDGET_MESSAGE) %>                
									</div>  
									<div class="modal-footer">  
										<a href="dc?method=delete&widget=<%=((String)widget[0]) %>" class="btn btn-danger">Delete</a>
										<a href="#" class="btn" data-dismiss="modal">Cancel</a>  
									</div>  
								</div>
								<!-- Modal Window for Code QR operation -->
								<div id="qrcode<%=((String)widget[0])%>" class="modal hide fade in" style="display: none;">  
									<div class="modal-body text-center content">  
										<p>QR Code of the widget. Scan the code with your mobile device to view the widget.</p>
										<img src="https://chart.googleapis.com/chart?cht=qr&chs=150x150&chl=<%=ApplicationProperties.getProperty(Constants.APP_URL)%>/r?i=<%=((String)widget[0])%>"
											alt="QR-Code"/>               
									</div>  
									<div class="modal-footer">  
										<a href="#" class="btn" data-dismiss="modal">Close</a>  
									</div>  
								</div>
								<%	} %>
							</tr>
						</table>
					<% }%>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	<!-- Footer -->
	<%@ include file="footer.html"%>
	<!-- End Footer -->
	<script src="assets/js/jquery/jquery.js" type="text/javascript"></script>
	<script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
