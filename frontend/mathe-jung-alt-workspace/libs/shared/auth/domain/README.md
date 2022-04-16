# shared-auth-domain

einloggen, registrieren, ausloggen

## Login flow

__Falls der authprovider verfügbar ist:__

In environment wird die property withFakeLogin auf false gesetzt. Diese wird im app.module.ts die AuthConfiguration übernommen.

Die Methode, die aufgerufen werden muss, ist AuthFacade.requestLoginRedirectUrl(). Diese Methode triggert die action  requestLoginRedirectUrl.

Dann laufen folgende Schritte nacheinander ab:

* der Effect requestLoginRedirectUrl$ lauscht auf requestLoginRedirectUrl und ruft im Backend '/authurls/login' auf und bekommt die Url mit nonce in Message.message zurück.
  
* mit dieser URL triggert der Effect die Action getRedirectLoginUrlSuccess
  
* der Effect redirectToLogin$ lauscht auf die Action getRedirectLoginUrlSuccess und redirected zum Loginserver (opa-wetterwachs.de/auth-app)

* Nach dem Login vom Loginserver wird die app-Url mit den session-credentials AuthResult nach dem # aufgerufen. app.component.ts parsed den url.hash um ihn in ein AuthResult umzuwandeln und ruft damit in der AuthFacade die Methode createSession auf. Diese dispatcht die gleichnamige action

* der Effect createSession$ lauscht auf diese Action und ruft im backend die URL '/login' auf.

* bei erfolgreichem login schreibt der effect die Session in den localStorage, dispatcht die action userLoggedIn und navigiert zur loginSuccessUrl aus der Configuration

__Abkürzung ohne authprovider__

In environment wird die property withFakeLogin auf true gesetzt. Diese wird im app.module.ts in die AuthConfiguration übernommen.

Die Methode, die aufgerufen werden muss, ist AuthFacade.createFakeSession().

Dann laufen folgende Schritte nacheinander ab:

* der Effect createSession$ lauscht auf diese Action und (wegen withFakeLogin === true) die Methode AuthHttpService.createFakeSession() auf.

* bei erfolgreichem login schreibt der effect die Session in den localStorage, dispatcht die action userLoggedIn und navigiert zur loginSuccessUrl aus der Configuration


