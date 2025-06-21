
# README – Εκτέλεση Kubernetes Deployment

## 1. Προϋποθέσεις

• Εγκατεστημένο Kubernetes cluster (μέσω minikube)  
• Προσβάσιμο `kubectl` CLI και σωστό context για το cluster  
• Ενεργό Ingress Controller (π.χ. nginx ingress controller)  
• Εγκατεστημένο cert-manager (για SSL)  

## 2. Βήματα Εκτέλεσης

Εκτελέστε τις παρακάτω εντολές με τη σειρά, από τον φάκελο που περιέχει τα YAML αρχεία σας.

### Βήμα 1: Εγκατάσταση PostgreSQL

```bash
kubectl apply -f postgres-pvc.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f postgres-svc.yaml
```

### Βήμα 2: Εγκατάσταση Spring Boot Εφαρμογής

```bash
kubectl apply -f spring-deployment.yaml
kubectl apply -f spring-svc.yaml
```

### Βήμα 3: Ρύθμιση Ingress 

```bash
kubectl apply -f spring-ingress.yaml
```

### Βήμα 4: Προσθήκη Let's Encrypt Issuer

```bash
kubectl apply -f Cluster-issuer.yaml
```

### Βήμα 5: Ενεργοποίηση Ingress με SSL

```bash
kubectl apply -f spring-ingress-ssl.yaml
```

## 3. Πρόσβαση στην εφαρμογή

Αν όλα έχουν ρυθμιστεί σωστά, η εφαρμογή θα είναι προσβάσιμη μέσω του domain που έχει δηλωθεί στο `spring-ingress-ssl.yaml`.

**Παράδειγμα:** `https://spring.rentestate.ip-ddns.com`
