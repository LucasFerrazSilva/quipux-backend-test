# Quipux Back-end Test

## Descrição do teste

Implemente utilizando MAVEN, JPA, em sua IDE Favorita e usando as bibliotecas Java de sua escolha um conjunto de API 
cujo contrato está detalhado abaixo:

<table>
    <thead>
        <tr>
            <th>HTTP</th>
            <th>Modelo URI</th>
            <th>Descrição</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/lists</td>
            <td>
                Adicionar uma nova lista de reprodução. <br>
                Se adicionado com sucesso, retorna “201 Created” com a referência da URL e conteúdo da lista.<br>
                Se o nome da lista não é válido (ej: null) deve devolver o erro “400 Bad Request”.
            </td>
        </tr>
        <tr>
            <td rowspan="2">GET</td>
            <td>/lists</td>
            <td>Ver todas as listas de reprodução existentes</td>
        </tr>
        <tr>
            <td>/lists/{listName}</td>
            <td>
                Ver a descrição de uma lista de reprodução selecionada. <br>
                Se a lista não existe, deve devolver “404 Not Found”.
            </td>
        </tr>
        <tr>
            <td>DELETE</td>
            <td>/lists/{listName}</td>
            <td>
                Apagar uma lista de reprodução.<br>
                Se for bem sucedido deve retornar: “204 No Content”.<br>
                Se a lista não existir deve retornar “404 Not Found”.
            </td>
        </tr>
    </tbody>
</table>

Para a implementação da API, ter em conta:

* A representação JSON dos recursos de serviço é:

**Lista de Reprodução**: Uma lista de reprodução tem nome, descrição e um conjunto músicas.

```JSON
{
    "nome": "Lista 1",
    "descrição": "Lista de musicas do Spotify",
    "músicas": [
        {
            "titulo": "",
            "artista": """album": "",
            "ano": "",
            "genero": ""
        },
        {
            "titulo": "",
            "artista": "",
            "album": "",
            "ano": "",
	        "genero": ""
        },
    ...
    ]
}
```

**Música**: Cada música tem um título, nome do artista, álbum, ano e gênero.

```JSON
{
    "titulo": "",
    "artista": "",
    "album": "",
    "ano": "",
    "genero": ""
}
```

* Implementar autenticação e autorização.
* Implementar uma camada de persistencia em um banco de dados runtime (Sugerencia h2).
* Testar o aplicativo usando uma das extensões REST no navegador (por exemplo, Advanced REST Client para Chrome ou PostMan) e entregar client configurado.
* Implementar testes unitarios.
