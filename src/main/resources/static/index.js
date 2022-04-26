const apo = new Vue({
    el: '#app',
    data: {
        rfcNames: [],
        currentRfcs: [
            {
                name: "",
                content: [],
            }
        ],
        textToSearch: ""
    },
    methods: {
        getRfcContent(rfcName) {
            axios.get('http://localhost:8080/rfc/' + rfcName)
                .then(response => {
                    this.currentRfcs = [{ name: "", content: [] }];
                    let data = response.data[0];
                    this.currentRfcs = [{ name: data.filename, content: data.content }];
                })
                .catch(error => {
                    alert('error: ' + error);
                    this.currentRfcs = [
                        {
                            name: rfcName,
                            content: [{ line: 1, text: "Was not possible to get this RFC content"}]
                        }
                    ];
                });
        },

        findRfcWithText(textToSearch) {
            axios.get('http://localhost:8080/rfc/find?withText=' + textToSearch)
                .then(response => {
                    this.currentRfcs = [];
                    response.data.forEach(data => {
                        this.currentRfcs.push({ name: data.filename, content: data.content });
                    });
                })
                .catch(error => {
                    alert('error: ' + error);
                    this.currentRfcs = [
                        {
                            name: "Not found",
                            content: [{ line: 1, text: "Was not possible to find text on RFC contents"}]
                        }
                    ];
                });
        }
    },
    mounted () {
        this.$nextTick( () => {
            axios.get('http://localhost:8080/rfc/all')
                .then(response => {
                    this.rfcNames = response.data;
                })
                .catch(error => {
                    console.log(error);
                    this.rfcNames = ['Was not possible to get te RFCs list'];
                })
        })
    }
});