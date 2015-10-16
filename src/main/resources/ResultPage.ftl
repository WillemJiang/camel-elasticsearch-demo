<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Camel with Elasticsearch</title>
    <style type="text/css">
        /* reset */
        html {
            font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
            font-size: 16px;
            -ms-text-size-adjust: 100%; /* 2 */
            -webkit-text-size-adjust: 100%; /* 2 */
            line-height:2;
        }
        body {
            margin: 0;
        }
        h1 {
            font-size: 2em;
            margin: 0.67em 0;
        }
        table {
            border-collapse: collapse;
            border-spacing: 0;
        }

        td, th {
            padding: 0;
        }
        .container{
            padding-right: 15px;
            padding-left: 15px;
            margin-right: auto;
            margin-left: auto
        }
        .table {
        width: 100%;
        max-width: 100%;
        margin-bottom: 20px
        }

        .table>tbody>tr>td,.table>tbody>tr>th,.table>tfoot>tr>td,.table>tfoot>tr>th,.table>thead>tr>td,.table>thead>tr>th {
        padding: 8px;
        line-height: 1.42857143;
        vertical-align: top;
        border-top: 1px solid #ddd
        }

        .table>thead>tr>th {
        text-align:left;
        font-weight: bold;
        vertical-align: bottom;
        border-bottom: 2px solid #ddd
        }

        .table>caption+thead>tr:first-child>td,.table>caption+thead>tr:first-child>th,.table>colgroup+thead>tr:first-child>td,.table>colgroup+thead>tr:first-child>th,.table>thead:first-child>tr:first-child>td,.table>thead:first-child>tr:first-child>th {
        border-top: 0
        }

        .table>tbody+tbody {
        border-top: 2px solid #ddd
        }
        .table-striped>tbody>tr:nth-of-type(odd) {
        background-color: #f9f9f9
        }
    </style>
</head>
 <body>
     <div class="container">
         <h1>Camel with Elasticsearch</h1>
         <table class="table table-striped">
             <thead>
                 <tr>
                     <th> Title </th>
                     <th> Description </th>
                 </tr>
             </thead>
             <tbody>
                 ${body}
             </tbody>
         </table>
     </div>
 </body>
 </html>
