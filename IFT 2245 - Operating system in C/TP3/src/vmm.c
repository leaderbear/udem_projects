#include <fcntl.h>
#include <stdio.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>

#include "conf.h"
#include "common.h"
#include "vmm.h"
#include "tlb.h"
#include "pt.h"
#include "pm.h"

static unsigned int read_count = 0;
static unsigned int write_count = 0;
static FILE* vmm_log;

/*ma partie*/
// Add the following global variable at the beginning of vmm.c
static unsigned int fifo_frame_index = 0;

// Function to choose a free frame or replace one using FIFO
unsigned int choose_frame_fifo() {
    unsigned int chosen_frame = fifo_frame_index;
    fifo_frame_index = (fifo_frame_index + 1) % NUM_FRAMES;
    return chosen_frame;
}
/*ma partie*/


void vmm_init (FILE *log)
{
  // Initialise le fichier de journal.
  vmm_log = log;
}


// NE PAS MODIFIER CETTE FONCTION
static void vmm_log_command (FILE *out, const char *command,
                             unsigned int laddress, /* Logical address. */
		             unsigned int page,
                             unsigned int frame,
                             unsigned int offset,
                             unsigned int paddress, /* Physical address.  */
		             char c) /* Caractère lu ou écrit.  */
{
  if (out)
    fprintf (out, "%s[%c]@%05d: p=%d, o=%d, f=%d pa=%d\n", command, c, laddress,
	     page, offset, frame, paddress);
}

/* Effectue une lecture à l'adresse logique `laddress`.  */
char vmm_read (unsigned int laddress)
{
  char c = '!';
  read_count++;
  /* ¡ TODO: COMPLÉTER ! */

  unsigned int page_number = laddress / PAGE_FRAME_SIZE;
  unsigned int offset = laddress % PAGE_FRAME_SIZE;

  int frame_number = tlb_lookup(page_number, false);
  if (frame_number < 0) {
    frame_number = pt_lookup(page_number);
    if (frame_number < 0) {
      frame_number = choose_frame_fifo();
      pm_download_page(page_number, frame_number);
      pt_set_entry(page_number, frame_number);
      tlb_add_entry(page_number, frame_number, pt_readonly_p(page_number));
    }
  }

  unsigned int physical_address = frame_number * PAGE_FRAME_SIZE + offset;
  c = pm_read(physical_address);


  // TODO: Fournir les arguments manquants.
  vmm_log_command(vmm_log, "READING", laddress, page_number, frame_number, offset, physical_address, c);
  return c;
}

/* Effectue une écriture à l'adresse logique `laddress`.  */
void vmm_write (unsigned int laddress, char c)
{
  write_count++;
  /* ¡ TODO: COMPLÉTER ! */

  unsigned int page_number = laddress / PAGE_FRAME_SIZE;
  unsigned int offset = laddress % PAGE_FRAME_SIZE;

  int frame_number = tlb_lookup(page_number, true);
  if (frame_number < 0) {
    frame_number = pt_lookup(page_number);
    if (frame_number < 0) {
      frame_number = choose_frame_fifo();
      pm_download_page(page_number, frame_number);
      pt_set_entry(page_number, frame_number);
      tlb_add_entry(page_number, frame_number, pt_readonly_p(page_number));
    } else if (pt_readonly_p(page_number)) {
      error("Write access violation on read-only page %d", page_number);
    }
  }

  unsigned int physical_address = frame_number * PAGE_FRAME_SIZE + offset;
  pm_write(physical_address, c);
  // TODO: Fournir les arguments manquants.
  vmm_log_command(vmm_log, "WRITING", laddress, page_number, frame_number, offset, physical_address, c);
}


// NE PAS MODIFIER CETTE FONCTION
void vmm_clean (void)
{
  fprintf (stdout, "VM reads : %4u\n", read_count);
  fprintf (stdout, "VM writes: %4u\n", write_count);
}
