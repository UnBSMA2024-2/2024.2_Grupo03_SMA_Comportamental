export function httpGet(uri) {
    fetch(uri)
      .then(response => {
        if (!response.ok) {
          throw new Error('Erro na requisição: ' + response.status);
        }
        return response.json();
      })
      .then(data => {
        console.log(data);
      })
      .catch(error => {
        console.error('Erro:', error);
      });
}

