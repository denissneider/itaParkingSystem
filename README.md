# itaParkingSystem
Rešitev za parkirni sistem pi predmetu ITA
Pametno parkiranje – SmartParking

1️⃣ Opis sistema

Pametno parkiranje je mikrostoritveni sistem, ki voznikom omogoča enostavno iskanje in rezervacijo prostih parkirnih mest v realnem času. Sistem vključuje senzorje na parkiriščih, ki zaznavajo zasedenost mest, omogoča rezervacije in obdeluje plačila. Cilj sistema je optimizirati uporabo parkirnih mest in hitrejše iskanje parkirnega mesta.

2️⃣ Osnovne funkcionalnosti

✅ Možnost rezervacije parkirnega mesta
✅ Obdelava plačil za parkiranje
✅ Obveščanje uporabnikov o statusu rezervacije
✅ Pregled zgodovine parkiranja

3️⃣ Ciljne skupine uporabnikov

👤 Vozniki – iščejo prosta parkirna mesta, rezervirajo in plačujejo parkiranje.

4️⃣ Arhitektura in komunikacija komponent

Sistem temelji na mikrostoritveni arhitekturi, kjer posamezne storitve komunicirajo prek REST API, gRPC in MQTT klicev.

🔹 Parking Service – beleži stanje parkirišč (prosta/zasedena mesta)
🔹 Users Service – omogoča rezervacije parkirišč in ima bazo uporabnikov
🔹 Payment Service – obdeluje plačila za parkiranje
🔹 Frontend (web ali mobilna aplikacija) – uporabniški vmesnik za iskanje in rezervacijo parkirišč
