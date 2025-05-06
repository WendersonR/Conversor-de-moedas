import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Principal {
    private static final String API_KEY = "f8375df72e9db99b086bc242";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("*******************");
                System.out.println("Por favor digite seu nome: ");
                String nome = scanner.nextLine().trim().toUpperCase();
                System.out.println("*******************");
                System.out.println("\n**** Olá " + nome + " , Seja bem Vindo(a) =) ****");
                System.out.println("\n*******************");
                System.out.println("Escolha a moeda de origem para conversão: (BRL, USD, EUR) ");
                String moedaOrigem = scanner.nextLine().trim().toUpperCase();
                System.out.println("\n*******************");
                System.out.print("Digite o valor: ");
                double valor = Double.parseDouble(scanner.nextLine().replace(",", ".")); // lê como string, depois converte
                System.out.println("\n*******************");
                System.out.print("Digite a moeda de destino (ex: BRL, USD, EUR, JPY): ");
                String moedaDestino = scanner.nextLine().trim().toUpperCase();
                System.out.println("*******************");

                String apiUrl = "https://v6.exchangerate-api.com/v6/"  + API_KEY + "/latest/" + moedaOrigem;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                    JsonObject taxas = json.getAsJsonObject("conversion_rates");

                    if (taxas.has(moedaDestino)) {
                        double taxa = taxas.get(moedaDestino).getAsDouble();
                        double valorConvertido = valor * taxa;

                        System.out.printf("Valor convertido: %.2f" + moedaOrigem + "= %.2f %s%n", valor, valorConvertido, moedaDestino);
                    } else {
                        System.out.println("Moeda de destino não encontrada.");
                    }
                } else {
                    System.out.println("Erro ao acessar a API. Código HTTP: " + response.statusCode());
                }

            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número, por exemplo: 100.50");
            } catch (IOException | InterruptedException e) {
                System.out.println("Erro na conexão com a API: " + e.getMessage());
            }

            System.out.println("\n*******************");
            System.out.println("Digite 1 para converter novamente ou 2 para sair:");
            String opcao = scanner.nextLine();

            if (opcao.equals("2")) {
                System.out.println("*******************");
                System.out.println("\nEncerrando o programa.");

                break;
            }
}
            scanner.close();
    }
}
