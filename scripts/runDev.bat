cd ../spa
rem Execute ng build --prod only if execution of npm install has finished successfully
npm install && ng build --configuration=dev
