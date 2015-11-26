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
            <form id="hacked_conciliacion_form" name="hacked_conciliacion_form" action="<g:createLink controller="home" action="conciliar"/>" method="post">
                <div class="row">
                    <div class="col-sm-4">
                        <div class='input-group control-group'>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                            <div class="controls">
                                <input type='text' id='date' name='date' class="form-control" placeholder="Fecha" readonly/>
                            </div>
                        </div>
                    </div>
                </div>
                <br/><br/><br/>
                <div class="row">
                    <div class="col-sm-4">
                        <div class='input-group control-group'>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-barcode"></span>
                            </span>
                            <div class="controls">
                                <input type='text' id='pax' name='pax' class="form-control" placeholder="Número de transacciones (#PAX)"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class='input-group control-group'>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-usd"></span>
                            </span>
                            <div class="controls">
                                <input type='text' id='amount' name='amount' class="form-control" min="0.01" step="0.01" placeholder="Monto"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class='input-group control-group'>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-usd"></span>
                            </span>
                            <div class="controls">
                                <input type='text' id='amountPaid' name='amountPaid' class="form-control" min="0.01" step="0.01" placeholder="Monto pagado"/>
                            </div>
                        </div>
                    </div>
                </div>
                <br/><br/>
                <div class="form-actions">
                    <input type="hidden" name="save" value="contact">
                    <button type="submit" class="btn btn-primary" id="submit_form_button">Enviar</button>
                </div>
            </form>
            <div id="success" style="display: none;"><div class="alert alert-success" role="alert"></div></div>
            <div id="error" style="display: none;"><div class="alert alert-danger" role="alert"></div></div>
        </div>
    </div>
</div>

<g:javascript library="jquery.min"/>
<g:javascript library="bootstrap.min"/>
<g:javascript library="moment"/>
<g:javascript library="bootstrap-datepicker"/>
<g:javascript library="autoNumeric-min"/>
<g:javascript library="jquery.validate.min"/>
<g:javascript library="additional-methods"/>
<g:javascript library="ie10-viewport-bug-workaround"/>

<script type="text/javascript">
    jQuery(document).ready(function () {

        $("#hacked_conciliacion_form").validate({
            rules: {
                date: {
                    minlength: 2,
                    required: true
                },
                pax: {
                    minlength: 1,
                    required: true
                },
                amount: {
                    minlength: 2,
                    required: true
                }
            },
            highlight: function (element) {
                $(element).closest('.controls').addClass('alert-danger').removeClass('alert-success');
            },
            success: function (element) {
                element.text('OK!').closest('.controls').addClass('alert-success').removeClass('alert-danger');
            }
        });
    });

    $(document).ready(function () {
        $('#date').datepicker({
            format: 'yyyy-mm-dd',
            weekStart: '1',
            autoclose: true,
            todayHighlight: true,
            endDate: "-1d"
        });

        $("#amount").autoNumeric('init', {aSep: '.', aDec: ',', aPad: false});
        $("#amountPaid").autoNumeric('init', {aSep: '.', aDec: ',', aPad: false});
        $("#pax").autoNumeric('init', {aSep: '.', aDec: ',', aPad: false});

        $("#submit_form_button").submit(function(e){

            var formData = {
                date: $('#date').val(),
                pax: $('#pax').val(),
                amount: $('#amount').val(),
                amountPaid: $('#amountPaid').val()
            };

            e.preventDefault();
            $.ajax({
                url : "<g:createLink controller="home" action="conciliar"/>",
                type: "POST",
                data : formData,
                dataType: "json",
                success: function(data, textStatus, jqXHR){
                    if(data.response){
                        $("#error").css("display", "none");
                        $("#success").css("display", "block");
                    } else {
                        $("#success").css("display", "none");
                        $("#error").css("display", "block");
                    }
                },
                error: function (jqXHR, textStatus, errorThrown){

                }
            });
        });
    });
</script>
</body>
</html>
