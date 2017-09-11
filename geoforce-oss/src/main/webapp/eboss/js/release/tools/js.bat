for /f %%i in (login.txt) do type %%i >> login-debug.js    
java -jar yuicompressor-2.4.2.jar --type js --charset utf-8 -o ..\login.js login-debug.js  
del login-debug.js /f /q

for /f %%i in (usermanage.txt) do type %%i >> user-debug.js    
java -jar yuicompressor-2.4.2.jar --type js --charset utf-8 -o ..\user.js user-debug.js  
del user-debug.js /f /q

pause