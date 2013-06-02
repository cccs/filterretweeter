# Filter Retweeter

Twitter-Bot, um Twitter-Suchergebnisse zu filtern und anschließend zu
retweeten.

## Konfiguration

Die Datei `src/main/resources/twitter4j.properties.example` nach
`src/main/resources/twitter4j.properties` kopieren und bearbeiten.
Die entsprechenden Schlüssel erhält man, indem man sich mit dem
Ziel-Twitter-Account auf der
[Twitter-Entwickler-Seite](https://dev.twitter.com/apps) anmeldet und
eine neue Anwendung anlegt.

## Installation

Mittels

    ./gradlew installApp

wird die Anwendung übersetzt und im Verzeichnis `build/install` samt
Startskript abgelegt.

## Ideen

Erweiterungsmöglichkeiten? Gibt es viele!

* Konfiguration der Filter in Config-Datei, die bei Änderung neu
  eingelesen wird
* Verbesserte Ausgabe: Unterscheide zwischen „planmäßig gefilterten“
  Tweets (z.B. die, die vom Bot schon retweetet wurden) und
  „böswilligen/unpassenden“ (z.B. von Accounts, die ein Filterkriterium
  für einen anderen Kontext verwenden), um so das Greifen der
  Filterkonfiguration besser prüfen zu können
* Adaptive Poll-Zeite und Query-Länge: Je nach Anzahl der neuen Tweets
  die Poll-Zeit (und ggfs. Query-Länge) anpassen, um das Risiko des
  API-Limits zu reduzieren
* ...

## Lizenz

Geschrieben von Stefan Schlott. Das Programm steht unter der [GPL
v2](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html).

