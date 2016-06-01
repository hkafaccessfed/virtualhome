<!doctype html>
<html>
  <head>
    <meta name="layout" content="login" />
  </head>
  <body>

      <div class="row">
        <div id="description" class="span6">
          <h2>Welcome to the HKAF Virtual Home</h2>
          <p> The HKAF Virtual Home enables small organisations, including collaborative research facilities, to manage user identities for international, government and industry based researchers, as well as collaborators who are sponsored by an HKAF subscribed organization.</p>

          <p>Users are provided with:</p>
          <ul>
            <li>Access to federated services;</li>
            <li>A view of account details to assist with maintenance of your personal information;</li>
            <li>Secure management of passwords;</li>
            <li>The ability to reset a forgotten password 24/7 with our simple online tools; and</li>
            <li>Support for multiple browsers and mobile devices to assist you logging in wherever you are.</li>
          </ul>

          <p>Administrators are provided with:</p>
          <ul>
            <li>An easier to use and navigate administrative interface;</li>
            <li>Per organisation and per group scopes for authentication assertions;
            <li>Better security for your users. Password resets use one time codes instead of having you set something on their behalf; and</li>
            <li>The ability to manage many more groups and users than was previously possible.</li>
          </ul>

          <br><br>
          <center><r:img file="loginlogos.png" width="304" height="297"/></center>
        </div>

        <div id="loginform" class="span5 offset1">
          <g:render template="/loginform" />
        </div>
      </div>
  </body>
</html>
