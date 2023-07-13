import numpy as np

from matplotlib import pyplot as plt
from sklearn import neighbors
from sklearn.cluster import AgglomerativeClustering
from sklearn.decomposition import KernelPCA
from sklearn.manifold import Isomap
from sklearn.metrics import accuracy_score
from sklearn_extra.cluster import KMedoids  # pip install scikit-learn-extra


# Algorithmes

# k-plus proches voisins
def knn(voisins, train_matrice, test_matrice, y_train, y_test):
    clf = neighbors.KNeighborsClassifier(n_neighbors=voisins, metric='precomputed', algorithm='brute')

    clf.fit(train_matrice, y_train)
    knn_test = clf.predict(test_matrice)

    print("Le rapport de précision pour knn est: \n" + str(accuracy_score(y_test, knn_test)))

    ax = plt.figure(figsize=(12, 6)).add_subplot(121)
    ax.set_title('KNN sur la matrice dissimilarité test')

    for i in range(voisins):
        test_cluster = test_matrice[np.where(knn_test == i)[0]]
        ax.scatter(test_cluster[:, 0], test_cluster[:, 1])
    plt.show()


# k-médoides
def kmd(nbClusters, train_matrice, test_matrice):
    kmedoids_instance = KMedoids(n_clusters=nbClusters, random_state=0)
    kmedoids_instance.fit(train_matrice)

    closest_clusters_test = kmedoids_instance.predict(test_matrice)

    ax = plt.figure(figsize=(12, 6)).add_subplot(121)
    ax.set_title('K-médoïde sur la matrice dissimilarité test')

    for i in range(nbClusters):
        test_cluster = test_matrice[np.where(closest_clusters_test == i)[0]]
        ax.scatter(test_cluster[:, 0], test_cluster[:, 1])
        ax.scatter(kmedoids_instance.cluster_centers_[:, 0], kmedoids_instance.cluster_centers_[:, 1])

    plt.show()


# Isomap
def isomap(voisins, train_matrice, test_matrice):
    isomap = Isomap(n_neighbors=voisins, metric='precomputed')
    isomap.fit_transform(train_matrice)
    isomap_testset = isomap.transform(test_matrice)

    ax = plt.figure(figsize=(12, 8)).add_subplot(211)
    ax.set_title('Isomap sur la matrice dissimilarité test')
    ax.scatter(isomap_testset, np.zeros_like(isomap_testset))
    plt.show()


# PCoA
def pcoa(components, train_matrice, test_matrice):
    pcoa = KernelPCA(n_components=components, kernel='precomputed')
    pcoa.fit_transform(-.5 * train_matrice ** 2)
    pcoa_testset = pcoa.transform(-.5 * test_matrice ** 2)

    ax = plt.figure(figsize=(12, 8)).add_subplot(211)
    ax.set_title('PCoA sur la matrice dissimilarité test')
    ax.scatter(pcoa_testset, np.zeros_like(pcoa_testset))
    plt.show()


# Regroupement hiérarchique
def agglomerative_clustering_predict(agglomerative_clustering, dissimilarity_matrix):
    average_dissimilarity = list()
    for i in range(agglomerative_clustering.n_clusters):
        ith_clusters_dissimilarity = dissimilarity_matrix[:, np.where(agglomerative_clustering.labels_ == i)[0]]
        average_dissimilarity.append(ith_clusters_dissimilarity.mean(axis=1))
    return np.argmin(np.stack(average_dissimilarity), axis=0)


def hierarchique(clusters, train_matrice, test_matrice):
    agglomerative_clustering = AgglomerativeClustering(n_clusters=clusters, affinity='precomputed', linkage='average')
    agglomerative_clustering.fit(train_matrice)
    agglo_testset = agglomerative_clustering_predict(agglomerative_clustering, test_matrice)

    ax = plt.figure(figsize=(12, 6)).add_subplot(121)
    ax.set_title('Regroupement hiérarchique sur la matrice dissimilarité test')

    for i in range(clusters):
        test_cluster = test_matrice[np.where(agglo_testset == i)[0]]
        ax.scatter(test_cluster[:, 0], test_cluster[:, 1])
    plt.show()

