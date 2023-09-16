Vue.createApp({
    data() {
        return {
            errorToats: null,
            errorMsg: null,
            accountType: "none",
        }
    },
    methods: {
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        create: function (event) {
            event.preventDefault();
            if (this.accountType === "none") {
                this.errorMsg = "You must select an account type";
                this.errorToats.show();
            } else {
                let config = {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }
                axios.post(`/api/clients/current/accounts/?accountType=${this.accountType}`, config)
                    .then(response => window.location.href = "/web/accounts.html")
                    .catch((error) => {
                        this.errorMsg = error.response.data;
                        this.errorToats.show();
                    })
            }
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
}).mount('#app')