<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Hack Conciliación Pullman - Voyhoy</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}" type="text/css">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'ie10-viewport-bug-workaround.css')}" type="text/css">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'dashboard.css')}" type="text/css">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-datepicker3.min.css')}" type="text/css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Pullman Conciliación</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="<g:createLink controller="home" action="index"/>">Conciliacion</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="container">
            <h1 class="page-header">Hack Conciliación Pullman</h1>
            <form id="form-hacked-conciliacion" action="<g:createLink controller="home" action="conciliar"/>" method="post">
                <div class="row">
                    <div class="col-xs-6">
                        <div class='input-group date'>
                            <input type='text' id='startDate' class="form-control" placeholder="Fecha Inicio" readonly/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class='input-group date'>
                            <input type='text' id='endDate' class="form-control" placeholder="Fecha Fin" readonly/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <br/><br/><br/>
                <div class="row">
                    <div class="col-xs-4">
                        <div class='input-group date'>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-barcode"></span>
                            </span>
                            <input type='text' id='pax' class="form-control" placeholder="Número de transacciones (#PAX)"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4">
                        <div class='input-group date'>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-usd"></span>
                            </span>
                            <input type='text' id='amount' class="form-control" min="0.01" step="0.01" placeholder="Monto"/>
                        </div>
                    </div>
                </div>
                <br/><br/>
                <button type="button" class="btn btn-primary" id="form-submit-button">Enviar</button>
            </form>
            <div id="success"></div>
            <div id="error"></div>
        </div>
    </div>
</div>

<g:javascript library="jquery.min"/>
<g:javascript library="bootstrap.min"/>
<g:javascript library="moment"/>
<g:javascript library="bootstrap-datepicker"/>
<g:javascript library="autoNumeric-min"/>
<g:javascript library="ie10-viewport-bug-workaround"/>

<script type="text/javascript">
    $(document).ready(function () {
        $('#startDate').datepicker({
            format: 'yyyy-mm-dd',
            weekStart: '1',
            autoclose: true,
            todayHighlight: true
        });
        $('#endDate').datepicker({
            format: 'yyyy-mm-dd',
            weekStart: '1',
            autoclose: true,
            todayHighlight: true
        });

        $("#amount").autoNumeric('init', {aSep: '.', aDec: ',', aPad: false});
        $("#pax").autoNumeric('init', {aSep: '.', aDec: ',', aPad: false});

        $(document).on("click", "#form-submit-button", function(){

            var formData = {
                startDate: $('#startDate').val(),
                endDate: $('#endDate').val(),
                pax: $('#pax').val(),
                amount: $('#amount').val()
            };

            $.ajax({
                url : "<g:createLink controller="home" action="conciliar"/>",
                type: "POST",
                data : formData,
                success: function(data, textStatus, jqXHR){
                    //data - response from server
                },
                error: function (jqXHR, textStatus, errorThrown){

                }
            });
        });
    });
</script>
</body>
</html>
