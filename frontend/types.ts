
export interface Author {
  name: string;
  avatarUrl: string;
}

export interface Article {
  id: string;
  author: Author;
  source?: string; // e.g., "Dev.to", "Smashing Mag"
  title: string;
  excerpt: string; // Remplacé subtitle par excerpt
  thumbnailUrl: string;
  date: string;
  readTime: string; // "5 min read"
  tags: string[]; // e.g., ["React", "CSS"]
  url?: string; // Link to the real source
}

export type Tab = string;

export type TextSize = 'normal' | 'large' | 'extra';
