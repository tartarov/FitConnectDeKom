package com.example.demo;

import ch.qos.logback.core.status.Status;
import com.nimbusds.jose.shaded.gson.*;
import dev.fitko.fitconnect.api.config.ApplicationConfig;
import dev.fitko.fitconnect.api.domain.model.event.SubmissionStatus;
import dev.fitko.fitconnect.api.domain.model.metadata.data.MimeType;
import dev.fitko.fitconnect.api.domain.model.replychannel.ReplyChannel;
import dev.fitko.fitconnect.api.domain.model.route.Area;
import dev.fitko.fitconnect.api.domain.model.route.Route;
import dev.fitko.fitconnect.api.domain.model.submission.SentSubmission;
import dev.fitko.fitconnect.client.RouterClient;
import dev.fitko.fitconnect.client.SenderClient;
import dev.fitko.fitconnect.client.bootstrap.ApplicationConfigLoader;
import dev.fitko.fitconnect.client.bootstrap.ClientFactory;
import dev.fitko.fitconnect.client.router.DestinationSearch;
import dev.fitko.fitconnect.client.sender.model.Attachment;
import dev.fitko.fitconnect.client.sender.model.SendableSubmission;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
@RestController
public class DemoApplication {

	//public static String[] leikaKeys = []
	public static ApplicationConfig config = ApplicationConfigLoader.loadConfigFromPath(Paths.get("src/main/resources/YAML/config.yml"));
	public static SenderClient senderClient = ClientFactory.getSenderClient(config);
	final RouterClient routerClient = ClientFactory.getRouterClient(config);

	public static String[] leikaSchluessel;
	public static String[] arsSchluessel;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello (@RequestParam(value="name", defaultValue = "World") String name){
		return String.format("Hello %s",name);
	}

	@PostMapping("/submission")
	public ResponseEntity<JSONObject> submit(@RequestBody String requestBody) {
		// Hier Code für die Verarbeitung des eingehenden JSON-Datenstrings einfügen

		//String destinationId = getDestinationId();

		//String schemaUri = getSchemaUri();

		String jsonData1 = "{ \"G06001617\": { \"F60000227\": \"Mustermann\", \"F60000230\": \"Musterfrau\", \"F60000228\": \"Max\", \"F60000279\": \"Muster\", \"F60000229\": \"Dr. rer. nat.\", \"F60000332\": \"m\", \"G60000083\": { \"F60000231\": 15, \"F60000232\": 5, \"F60000233\": 1980 }, \"F60000234\": \"Musterstadt\", \"Anschrift\": { \"Straße\": \"Musterstraße\", \"Hausnummer\": \"123\", \"Postleitzahl\": \"12345\", \"Ort\": \"Musterstadt\", \"Zusatzangaben\": \"Zusatzinfo\" }, \"F60000236\": \"000\", \"E-Mail\": \"max.mustermann@example.com\", \"F00002552\": \"Personalausweis\", \"F06003121\": \"base64_encoded_image\"}, \"F00001019\": \"base64_encoded_image2\", \"G60000025\": { \"F60000330\": \"2023-01-15\", \"F60000247\": \"Musterstadt\", \"F60000053\": \"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ECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlAUGmcnX1jQABAgQIECBAgEBRAUGmaGF1iwABAgQIECBAgEBlgf8FFT3e5bo5Hl8AAAAASUVORK5CYII=\" } , \"G06001618\": { \"F06000025\": 123456789, \"F60000292\": \"Straßenverkehrsamt Musterstadt\", \"F60000294\": \"2023-01-15\", \"G06001619\": { \"G06001674\": { \"F06000087\": true, \"F06000088\": true, \"F06000089\": false, \"F06000090\": true, \"F06000092\": true, \"F06000097\": true, \"F06000102\": true, \"F06000103\": true }, \"G06001675\": { \"F06000093\": true, \"F06000094\": true, \"F06000098\": true, \"F06000099\": true, \"FÜ_Gültigkeitsdatum_befristeter_Klassen\": \"2023-01-15\" }, \"G06001676\": { \"F06000095\": true, \"F06000096\": true, \"F06000100\": true, \"F06000101\": true, \"FÜ_Gültigkeitsdatum_befristeter_Klassen\": \"2023-01-15\" } }, \"F06000610\": \"2023-01-15\", \"Beschränkungen_und_Zusatzangaben\": \"Keine Beschränkungen\" }, \"Ausstellung_Verlust_Vernichtung\": { \"Diebstahlanzeige\": true } }";

		System.out.println("ResponseBody: " + requestBody);
		JsonObject jsonObject = JsonParser.parseString(requestBody.toString()).getAsJsonObject();
		System.out.println("jsonObject: " + jsonObject);
		// Extract the desired part
		JsonObject antragObject = jsonObject.getAsJsonObject("antrag");
		String antragString = antragObject.toString();
		String schemaUriString = jsonObject.get("schemaUri").getAsString();
		String destinationID = jsonObject.get("destinationID").getAsString();
		String leikaKey = jsonObject.get("leikaKey").getAsString();

		// Print the extracted part
		System.out.println(antragString);
		System.out.println(schemaUriString);
		System.out.println(destinationID);
		System.out.println(leikaKey);

		final SendableSubmission sendableSubmission = SendableSubmission.Builder()
				.setDestination(UUID.fromString(destinationID))//d2d43892-9d9c-4630-980a-5af341179b14
				.setServiceType("urn:de:fim:leika:leistung:"+leikaKey, "FIT Connect Demo")
				.setJsonData(antragString, URI.create(schemaUriString))
				.addAttachment(Attachment.fromString("This is a text Attachment", "text/plain"))
				.setReplyChannel(ReplyChannel.fromEmail("themotherus@gmail.com")).build();

		final SentSubmission sentSubmission = senderClient.send(sendableSubmission);

		JSONObject responseData = new JSONObject();
		responseData.put("message", "Submission received successfully!");
		responseData.put("sentSubmission" , sentSubmission);
		responseData.put("caseID", sentSubmission.getCaseId());
		responseData.put("submissionID", sentSubmission.getSubmissionId());
		responseData.put("submissionStatus", senderClient.getStatusForSubmission(sentSubmission).getStatus());

		return ResponseEntity.ok(responseData);
	}

	@GetMapping("/findAreaID")
	public ResponseEntity<String> findAreaID (@RequestParam(value = "name") String name, @RequestParam(value= "zip" ) String zip) {
		final List<Area> areas = routerClient.findAreas(List.of(name, zip), 0, 5);

		System.out.println("Found {} areas: " + areas.size());
		for (final Area area : areas) {
			System.out.println("Area {} with id {} found: " + area.getName() + ", " + area.getId());
			return ResponseEntity.ok("Area {} with id {} found: " + area.getName() + ", " + area.getId());
		}

		return ResponseEntity.ok("Nothing found");
	}

	@GetMapping("/findDestinationID")
	public ResponseEntity<String> findDestinationID(){ //@RequestParam(value = "leika") String leika, @RequestParam(value= "areaid" ) String areaid

		String [] keys = getAllLeikaKeys();
		String [] arsKeys = getAllArsKeys();

		System.out.println("Unser Leika Keys sind: " + Arrays.toString(keys));
		System.out.println("FoundArsKey: " + Arrays.toString(arsKeys));

		int i=0;
	//	for (String singleArsKey : arsKeys) {

			for (String singleLeikaKey : keys) {
				String realLeikaString = singleLeikaKey.substring(1, singleLeikaKey.length()-1);
			//	String realArsString = singleArsKey.substring(1, singleArsKey.length()-1);

				final DestinationSearch search = DestinationSearch.Builder()
						.withLeikaKey(realLeikaString)
						.withArs("120660196196")// areaId der Stadt "Leipzig"
						.withLimit(3)
						.build();

// Finde die ersten drei Resultate
				final List<Route> routes = routerClient.findDestinations(search);

				System.out.println("Found " +  routes.size() + " routes for service identifier " + realLeikaString + " and ARS-Key " + "064350014014" + "counter: " + i++);
				for (final Route route : routes) {
					System.out.println("Route {} with destinationId {} found: " + route.getDestinationName() + ", " + route.getDestinationId());
					//return ResponseEntity.ok("Route {} with destinationId {} found: " + route.getDestinationName() + ", " + route.getDestinationId());
				}
			}
		//}
		return ResponseEntity.ok("someting went wrong");
	}

	private static String getDestinationId() {
		try {
			String urlString = "https://routing-api-testing.fit-connect.fitko.dev/v1/routes?ars=064350014014&leikaKey=99123456760610";

			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println("Response: " + response.toString());

			JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
			String destinationId = jsonObject
					.getAsJsonArray("routes").get(0)
					.getAsJsonObject().get("destinationId").getAsString();

			String schemaUri = jsonObject
					.getAsJsonArray("routes").get(0)
					.getAsJsonObject().getAsJsonObject("destinationParameters")
					.getAsJsonArray("submissionSchemas").get(0)
					.getAsJsonObject().get("schemaUri").getAsString();


			System.out.println("Schema URI: " + schemaUri);

			return destinationId;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String[] getAllLeikaKeys () {
		try {
		String urlString = "https://www.xrepository.de/api/xrepository/urn:de:fim:leika:leistung_20240219/download/FIMLeiKaLeistungen_20240219.json";

		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();
		System.out.println("Response Code: " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

			JsonArray datenArray = jsonObject.getAsJsonArray("daten");

			// Leika-Schlüssel in ein Array legen
			leikaSchluessel = new String[datenArray.size()];
			for (int i = 0; i < datenArray.size(); i++) {
				JsonElement row = datenArray.get(i);
				leikaSchluessel[i] = row.getAsJsonArray().get(0).toString();
			}

			System.out.println("FoundleikaKey: " + leikaSchluessel);

		return leikaSchluessel;
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	}
    }

	private static String[] getAllArsKeys () {
		try {
			String urlString = "https://www.xrepository.de/api/xrepository/urn:de:bund:destatis:bevoelkerungsstatistik:schluessel:rs_2021-01-31/download/Regionalschl_ssel_2021-01-31.json";

			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			System.out.println("Response Code: " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

			JsonArray datenArray = jsonObject.getAsJsonArray("daten");

			// Leika-Schlüssel in ein Array legen
			arsSchluessel = new String[datenArray.size()];
			for (int i = 0; i < datenArray.size(); i++) {
				JsonElement row = datenArray.get(i);
				arsSchluessel[i] = row.getAsJsonArray().get(0).toString();
			}

			return arsSchluessel;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/getSchemaUri")
	private static ResponseEntity<JSONObject> getSchemaUri(@RequestParam(value="leikaKey", defaultValue = "World") String leikaKey) {
		try {
            leikaKey = leikaKey.replace("\"", "");
			System.out.println(Long.parseLong(leikaKey));
				String urlString = "https://routing-api-testing.fit-connect.fitko.dev/v1/routes?ars=064350014014&leikaKey=" + Long.parseLong(leikaKey); //99001001000000  99123456760610 https://schema.fitko.de/fim/s00000092_1.0.schema.json

				URL url = new URL(urlString);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");

				int responseCode = connection.getResponseCode();
				System.out.println("Response Code: " + responseCode);

			if (responseCode != 200) {
				// Handle response code 500
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String errorInputLine;
				StringBuffer errorResponse = new StringBuffer();

				while ((errorInputLine = errorReader.readLine()) != null) {
					errorResponse.append(errorInputLine);
				}
				errorReader.close();

				System.out.println("Error Response: " + errorResponse.toString());

				JSONObject responseData = new JSONObject();
				responseData.put("Error", errorResponse.toString());
				return ResponseEntity.ok(responseData);
				// You may choose to return or handle the error response here
			} else  {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
				String schemaUri;
				String destinationId;

				// Extract schemaUri and destinationId based on the condition
				if (!leikaKey.equals("99001001000000")) {
					JsonObject route = jsonObject.getAsJsonArray("routes").get(0).getAsJsonObject();
					schemaUri = route.getAsJsonObject("destinationParameters")
							.getAsJsonArray("submissionSchemas").get(0).getAsJsonObject().get("schemaUri").getAsString();
					destinationId = route.get("destinationId").getAsString();
				} else {
					JsonObject route = jsonObject.getAsJsonArray("routes").get(1).getAsJsonObject();
					schemaUri = route.getAsJsonObject("destinationParameters")
							.getAsJsonArray("submissionSchemas").get(0).getAsJsonObject().get("schemaUri").getAsString();
					destinationId = route.get("destinationId").getAsString();
				}


				System.out.println("Schema URI: " + schemaUri);
				System.out.println("Destination ID: " + destinationId);

				JSONObject responseData = new JSONObject();
				responseData.put("schemaUri", schemaUri);
				responseData.put("destinationID", destinationId);

				return ResponseEntity.ok(responseData);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	@PostMapping("/updateAntraege")
	private static ResponseEntity<JSONArray> updateAntraege(@RequestBody String requestBody) {
		System.out.println("requestBody: " + requestBody);
		JsonObject jsonObject = JsonParser.parseString(requestBody.toString()).getAsJsonObject();
		JsonArray antragArray = jsonObject.getAsJsonArray("antragArray");

		JSONArray statusArray = new JSONArray();

		for (int i = 0; i < antragArray.size(); i++) {
			String antragString = antragArray.get(i).toString();
			String cleanedJsonString = antragString.substring(1, antragString.length() - 1).replace("\\\"", "\"");
			System.out.println(cleanedJsonString);
			JsonObject antragObject = JsonParser.parseString(cleanedJsonString).getAsJsonObject();
			System.out.println(antragObject);
			UUID destinationId = UUID.fromString(antragObject.get("destinationId").getAsString());
			UUID submissionId = UUID.fromString(antragObject.get("submissionId").getAsString());
			UUID caseId = UUID.fromString(antragObject.get("caseId").getAsString());

			SentSubmission antragSentSubmission = SentSubmission.builder()
					.destinationId(destinationId)
					.submissionId(submissionId)
					.caseId(caseId)
					.build();

			SubmissionStatus submissionStatus = senderClient.getStatusForSubmission(antragSentSubmission);
			System.out.println(submissionStatus.getStatus());
			JSONObject statusObject = new JSONObject();
			statusObject.put(submissionId.toString(), submissionStatus.getStatus());

			// Füge das Objekt dem statusArray hinzu
			statusArray.add(statusObject);
		}
		return ResponseEntity.ok(statusArray);
	}
}
