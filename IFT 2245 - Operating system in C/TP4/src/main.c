#pragma clang diagnostic push
#pragma ide diagnostic ignored "OCUnusedGlobalDeclarationInspection"
#pragma ide diagnostic ignored "hicpp-signed-bitwise"
#pragma ide diagnostic ignored "readability-non-const-parameter"

#include "main.h"

uint8_t ilog2(uint32_t n) {
    uint8_t i = 0;
    while ((n >>= 1U) != 0)
        i++;
    return i;
}

//--------------------------------------------------------------------------------------------------------
//                                           DEBUT DU CODE
//--------------------------------------------------------------------------------------------------------

/**
 * Exercice 1
 *
 * Prend cluster et retourne son addresse en secteur dans l'archive
 * @param block le block de paramètre du BIOS
 * @param cluster le cluster à convertir en LBA
 * @param first_data_sector le premier secteur de données, donnée par la formule dans le document
 * @return le LBA
 */
uint32_t cluster_to_lba(BPB *block, uint32_t cluster, uint32_t first_data_sector) {
    uint32_t sectors_per_cluster = block->BPB_SecPerClus;
    return (cluster - 2) * sectors_per_cluster + first_data_sector;
}

/**
 * Exercice 2
 *
 * Va chercher une valeur dans la cluster chain
 * @param block le block de paramètre du système de fichier
 * @param cluster le cluster qu'on veut aller lire
 * @param value un pointeur ou on retourne la valeur
 * @param archive le fichier de l'archive
 * @return un src d'erreur
 */
error_code get_cluster_chain_value(BPB *block,
                                   uint32_t cluster,
                                   uint32_t *value,
                                   FILE *archive) {
    uint32_t fat_start_sector = as_uint16(block->BPB_RsvdSecCnt);
    uint32_t fat_offset = cluster * 4;
    uint32_t fat_sector = fat_start_sector + (fat_offset / as_uint16(block->BPB_BytsPerSec));
    uint32_t entry_offset = fat_offset % as_uint16(block->BPB_BytsPerSec);
    
    if (fseek(archive, fat_sector * as_uint16(block->BPB_BytsPerSec) + entry_offset, SEEK_SET) != 0) {
        return GENERAL_ERR;
    }
    
    uint8_t raw_entry[4];
    if (fread(raw_entry, sizeof(uint8_t), 4, archive) != 4) {
        return GENERAL_ERR;
    }
    
    *value = as_uint32(raw_entry) & 0x0FFFFFFF;
    return NO_ERR;
}

/**
 * Exercice 3
 *
 * Vérifie si un descripteur de fichier FAT identifie bien fichier avec le nom name
 * @param entry le descripteur de fichier
 * @param name le nom de fichier
 * @return 0 ou 1 (faux ou vrai)
 */
bool file_has_name(FAT_entry *entry, char *name) {
    char formatted_name[FAT_NAME_LENGTH + 1];
    memset(formatted_name, 0, FAT_NAME_LENGTH + 1);
    int i, j;
    for (i = 0; i < 8 && entry->DIR_Name[i] != ' '; i++) {
        formatted_name[i] = entry->DIR_Name[i];
    }
    if (entry->DIR_Name[8] != ' ') {
        formatted_name[i++] = '.';
        for (j = 8; j < 11 && entry->DIR_Name[j] != ' '; j++) {
            formatted_name[i++] = entry->DIR_Name[j];
        }
    }
    formatted_name[i] = '\0';

    return strcmp(formatted_name, name) == 0;
}

/**
 * Exercice 4
 *
 * Prend un path de la forme "/dossier/dossier2/fichier.ext et retourne la partie
 * correspondante à l'index passé. Le premier '/' est facultatif.
 * @param path l'index passé
 * @param level la partie à retourner (ici, 0 retournerait dossier)
 * @param output la sortie (la string)
 * @return -1 si les arguments sont incorrects, -2 si le path ne contient pas autant de niveaux
 * -3 si out of memory
 */
int break_up_path(char* path, uint8_t level, char** output) {
    if (!path || !output) {
        return GENERAL_ERR;
    }
    size_t path_len = strlen(path);
    if (path_len == 0) {return GENERAL_ERR;}

    size_t start_pos = 0;
    size_t end_pos = 0;
    size_t part_index = 0;

    bool found = false;
    while (!found) {
        if (path[end_pos] == '/' || path[end_pos] == '\0') {
            if (part_index == level) {
                found = true;
                size_t part_len = end_pos - start_pos;
                *output = (char*)malloc(part_len + 1);
                if (*output == NULL) {
                    return OUT_OF_MEM;
                }
                for (size_t i = 0; i < part_len; i++) {
                    (*output)[i] = toupper(path[start_pos + i]);
                }
                (*output)[part_len] = '\0';
                break;
            }
            start_pos = end_pos + 1;
            part_index++;
        }

        end_pos++;
    }
    return NO_ERR;
}

/**
 * Exercice 5
 *
 * Lit le BIOS parameter block
 * @param archive fichier qui correspond à l'archive
 * @param block le block alloué
 * @return un src d'erreur
 */
error_code read_boot_block(FILE *archive, BPB **block) {
    if (!archive || !block) {
        return GENERAL_ERR;
    }

    *block = (BPB *)malloc(sizeof(BPB));
    if (!(*block)) {
        return OUT_OF_MEM;
    }

    if (fseek(archive, 0, SEEK_SET) != 0) {
        return GENERAL_ERR;
    }

    if (fread(*block, sizeof(BPB), 1, archive) != 1) {
        return GENERAL_ERR;
    }

    return NO_ERR;
}

/**
 * Exercice 6
 *
 * Trouve un descripteur de fichier dans l'archive
 * @param archive le descripteur de fichier qui correspond à l'archive
 * @param path le chemin du fichier
 * @param entry l'entrée trouvée
 * @return un src d'erreur
 */
error_code find_file_descriptor(FILE *archive, BPB *block, char *path, FAT_entry **entry) {
    uint32_t first_data_sector = as_uint16(block->BPB_RsvdSecCnt) + (as_uint32(block->BPB_FATSz32) * block->BPB_NumFATs);

    char *next_path;
    error_code err = break_up_path(path, 0, &next_path);

    if (err < 0) {
        return err;
    }

    uint32_t current_cluster = as_uint32(block->BPB_RootClus);
    FAT_entry *current_entry = malloc(sizeof(FAT_entry));

    if (current_entry == NULL) {
        free(next_path);
        return OUT_OF_MEM;
    }

    bool found = false;

    while (!found) {
        uint32_t current_lba = cluster_to_lba(block, current_cluster, first_data_sector);
        fseek(archive, current_lba * as_uint16(block->BPB_BytsPerSec), SEEK_SET);

        for (uint32_t i = 0; i < as_uint16(block->BPB_BytsPerSec) / FAT_DIR_ENTRY_SIZE; i++) {
            fread(current_entry, FAT_DIR_ENTRY_SIZE, 1, archive);

            if (current_entry->DIR_Name[0] == 0x00) {
                break;
            }

            if (file_has_name(current_entry, next_path)) {
                found = true;
                break;
            }
        }

        if (!found) {
            uint32_t next_cluster;
            err = get_cluster_chain_value(block, current_cluster, &next_cluster, archive);

            if (err < 0 || next_cluster >= FAT_EOC_TAG) {
                free(current_entry);
                free(next_path);
                return RES_NOT_FOUND;
            }

            current_cluster = next_cluster;
        }
    }

    free(next_path);

    if (entry != NULL) {
        *entry = current_entry;
    } else {
        free(current_entry);
    }

    return NO_ERR;
}

/**
 * Exercice 7
 *
 * Lit un fichier dans une archive FAT
 * @param entry l'entrée du fichier
 * @param buff le buffer ou écrire les données
 * @param max_len la longueur du buffer
 * @return un src d'erreur qui va contenir la longueur des donnés lues
 */
error_code
read_file(FILE *archive, BPB *block, FAT_entry *entry, void *buff, size_t max_len) {
    if (!archive || !block || !entry || !buff) {
        return GENERAL_ERR;
    }

    uint32_t file_size = as_uint32(entry->DIR_FileSize);
    uint32_t file_cluster = (as_uint16(entry->DIR_FstClusHI) << 16) + as_uint16(entry->DIR_FstClusLO);

    if (max_len < file_size) {
        return OUT_OF_MEM;
    }

    uint32_t first_data_sector = as_uint16(block->BPB_RsvdSecCnt) + (block->BPB_NumFATs * as_uint32(block->BPB_FATSz32));
    uint32_t current_cluster = file_cluster;
    uint32_t remaining_bytes = file_size;
    uint32_t lba = 0;
    uint32_t cluster_size = block->BPB_SecPerClus * as_uint16(block->BPB_BytsPerSec);

    while (remaining_bytes > 0) {
        lba = cluster_to_lba(block, current_cluster, first_data_sector);

        fseek(archive, lba * as_uint16(block->BPB_BytsPerSec), SEEK_SET);

        uint32_t bytes_to_read = remaining_bytes > cluster_size ? cluster_size : remaining_bytes;
        fread(buff, 1, bytes_to_read, archive);

        remaining_bytes -= bytes_to_read;
        buff = (uint8_t *)buff + bytes_to_read;

        if (remaining_bytes > 0) {
            error_code err = get_cluster_chain_value(block, current_cluster, &current_cluster, archive);
            if (err < 0) {
                return err;
            }
        }
    }

    return file_size;
}

// ༽つ۞﹏۞༼つ

int main() {
// ous pouvez ajouter des tests pour les fonctions ici

    return 0;
}

// ༽つ۞﹏۞༼つ
