<html>
<head>
    <title>Login Webapp</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <div class="row justify-content-md-center h-100">
        <div class="col-sm-12 col-md-6 col-lg-4 my-auto">
            <h2>Login</h2>
            <p>${error}</p>
            <form action="/login" method="post">
                <div class="input-group mb-4 input-group-md">
                    <span class="input-group-text" id="username" style="width: 40px">
                        <i class="fa fa-user"></i>
                    </span>
                    <input type="text" class="form-control" name="username" placeholder="Username" aria-label="Username"
                           aria-describedby="username">
                </div>
                <div class="input-group mb-4 input-group-md">
                    <span class="input-group-text" id="password" style="width: 40px">
                        <i class="fa fa-key"></i>
                    </span>
                    <input type="password" class="form-control" name="password" placeholder="Password"
                           aria-label="Password" aria-describedby="password">
                </div>
                <div class="d-grid gap-2">
                    <button class="btn btn-primary" type="submit"><i class="fa fa-sign-in"></i> &nbsp; Login</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
