## Data Migration

Requires: Database Setup, General Web Server Setup, Tomcat Setup, Application Setup

1. To start the data migration process take a copy of your VHO database and store it locally. I tend to use the database name of 'old_vho_ENV' to ensure there is no confusion.

1. You will require an install of Groovy to continue grab one from http://groovy.codehaus.org/

1. Run the groovyConsole script in the groovy bin directory. You will need to also pass the MySQL jar to the instance to allow reading of the old VHO data which you've already downloaded in the Tomcat setup phase.

        $ ~/groovy > ./bin/groovyConsole -cp ~/mysql-connector-java-5.1.18.jar 

1. Copy and paste the file scripts/vho_export.groovy into your groovyConsole window. 

1. Setup your VHO specific config. e.g:

        // VHO specific config
        database = 'old_vho_prd'
        dbusername = 'root'
        dbpassword = ''

1. The script will process your VHO database and provide some details. Most importantly:

        JSON output of database old_vho_prd stored at /tmp/old_vho_prd.json
        A file ready to map FR organization ID stored at /tmp/old_vho_prd_fr_mapping.txt

1. Unfortunately the next part of the equation is a manual process. Open the generated /tmp/old_vho_prd_fr_mapping.txt file. Using your current VHO records and your FR deployment you need to match the previously used VHO organization identifier to an actual FR Organization ID. The ID of the organization is the last part of the show URL in your FR instance. For qcif the URL is `/federationregistry/membership/organization/show/299` so we have a file that looks like:

        qcif:299
        questnet:103
        qut:10

If any organization is **not supplied** an FR mapping all accounts linked to that Organization in your existing VHO deployment will not be migrated. You may need to create some Organizations in FR to satisfy the organizations in VHO or map multiple VHO organization to the same FR Organization ID.

1. Upload both created files to your webserver. I recommend /tmp and ensure that tomcatweb user can read the files.

1. Navigate to https://vho.example.edu.au/console

1. Copy and paste the bootstrap.groovy file in configuration/vhr to the console window. Create appropriate principal and apiKey values. You will require these later in your Shibboleth IdP configuration as the values for apiToken and apiSecret respectively.

1. Clear the script. Copy and paste scripts/vhr_import.groovy to the window.

1. Modify your mappingFile and dataFile variables according to the files you uploaded from the vho_export process.

1. Execute the script, ensure there are no errors.






admin rights
insert into base_role_subjects (subject_id, role_id) values(69,1);
