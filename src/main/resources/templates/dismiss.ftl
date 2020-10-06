<#ftl encoding="utf-8">
<!doctype html>
<html lang="en">
<head>
    <style>
        p.x{
            text-align: left;
            margin-left: 400px;
        }
        .z{
            text-align: center;
            font-size: 20px;
        }
        p.y{
            text-align: left;
            margin-left: 0;

        }
        p.a{
            text-align: left;
            margin-left: 300px;
        }
    </style>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Заявление на увольнение</title>
</head>
<body>
<p class="x">
    Генеральному директору<br>
    ООО "Светлое будущее"<br>
    Петрову П.П<br>
    от ${fullName}
</p>
<br>
<br>
<br>
<br>
<br>
<br>
<h1 class="z">Заявление</h1>
<h2 class="z">об увольнении по собственному желанию</h2>
<p class="y">
    Я ${fullName} пасспорт ${passportSeries} выдан ${passportDate} в соотсветствии со статьёй<br>
    80 Трудового кодекса Российской Федерации прошу уволить меня по собственному желанию<br>
    ${lastMainPartDate}<br>
</p>
<br>
<br>
<br>
<br>
<p class="a">
    ${endDateMainPart}<br>
    Подпись: _________________________________________<br>
</p>
</body>
</html>