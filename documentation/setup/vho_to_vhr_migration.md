# Create VHR database and user will full perms for use by VHR web application
    
    `CREATE DATABASE virtualhomeregistry DEFAULT CHARACTER SET utf8;`


# Create a secondary user for the Shibboleth IdP with read access to the VHR database and write access on VHR.shibpid
# Configure VHR via local config file and Tomcat context description based off examples in <todo>
# Start VHR web application - undertake initial login and provide created subject with admin rights

    `alter table virtualhomeregistry.shibpid change deactivationDate deactivationDate TIMESTAMP NULL;`

# Export VHO data using vho_export.groovy
# Place files created from the above process on the VHR server, /tmp is usually fine

# Access VHR admin console. Paste the vhr_import script and adjust variables as appropriate to point to above files
# Run the script, the environment should be created. If there are serious errors the transaction will be rolled back. For non serious errors you may need to manually manipulate some data.

# VHR should essentially be running all VHO accounts now and ready to service. You may like to do some manual spot checks of accounts and Org/Group admin rights.
